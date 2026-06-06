package com.pulse_gym.ms_notifications.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.client.AuthClient;
import com.pulse_gym.lb_common.client.UsuarioClient;
import com.pulse_gym.lb_common.dto.AuthUserDTO;
import com.pulse_gym.lb_common.dto.EnvioNotificacionDTO;
import com.pulse_gym.lb_common.dto.EnvioEventoNotificacionDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.notification.Notificacion;
import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;
import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEstadoNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;
import com.pulse_gym.ms_notifications.repository.NotificacionRepository;
import com.pulse_gym.ms_notifications.repository.PlantillaNotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificacionService {

    /**
     * Inyeccion de Logger para loguear los mensajes de la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    
    /**
     * Formato de fecha para la plantilla de notificaciones
     */
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Inyeccion de servicios de email 
     */
    private final EmailService emailService;

    /**
     * Inyeccion de servicios de whatsapp
     */     
    private final WhatsAppService whatsAppService;

    /**
     * Inyeccion de repositorio de notificaciones
     */
    private final NotificacionRepository notificacionRepository;

    /**
     * Inyeccion de repositorio de plantillas
     */
    private final PlantillaNotificationRepository plantillaRepository;

    /**
     * Inyeccion de servicio de renderizado de plantillas
     */
    private final PlantillaRenderService renderService;
    
    /**
     * Inyeccion de servicio de cliente de usuarios
     */
    private final UsuarioClient usuarioClient;
    
    /**
     * Inyeccion de servicio de cliente de auth
     */
    private final AuthClient authClient;
    
    /**
     * Inyeccion de servicio de preferencias de usuarios
     */
    private final PreferenciaUsuarioService preferenciaUsuarioService;

    /**
     * Inyeccion de servicio de rate limit
     */
    private final RateLimitService rateLimitService;

    /**
     * Envia una notificacion usando plantilla con variables dinamicas
     *
     * @param plantillaId           Identificador de la plantilla
     * @param usuarioAuthId         Identificador del usuario en auth
     * @param variablesAdicionales  Variables para renderizar la plantilla
     */
    public void enviarNotificacionConPlantilla(Long plantillaId, Long usuarioAuthId,
            Map<String, Object> variablesAdicionales) {

        PlantillaNotificacion plantilla = plantillaRepository.findById(plantillaId)
                .orElseThrow(() -> new RuntimeException("Plantilla no encontrada con id: " + plantillaId));

        if (Boolean.TRUE.equals(plantilla.getEliminada())) {
            throw new RuntimeException("La plantilla fue eliminada");
        }

        if (Boolean.FALSE.equals(plantilla.getEstado())) {
            throw new RuntimeException("La plantilla esta inactiva");
        }

        AuthUserDTO authUser = obtenerAuthUser(usuarioAuthId);
        UsuarioPerfilResponseDTO usuario = obtenerPerfilPorEmail(authUser.getEmail());

        EnumEventoAsociado evento = resolverEventoPlantilla(plantilla);
        EnumCanalNotificacion canal = plantilla.getTipoPlantilla();

        preferenciaUsuarioService.validarPreferenciasUsuario(usuarioAuthId, evento, canal);
        rateLimitService.validarLimiteEnvio(usuarioAuthId);

        Map<String, Object> contexto = construirContexto(usuario, authUser, variablesAdicionales);
        String contenidoRenderizado = renderService.renderizar(plantilla.getContenido(), contexto);

        EnvioNotificacionDTO dto = new EnvioNotificacionDTO();
        dto.setUsuarioId(usuarioAuthId);
        dto.setContenido(contenidoRenderizado);
        dto.setAsunto(plantilla.getTitulo());
        dto.setCanal(canal.name());
        dto.setPlantillaId(plantillaId);
        dto.setTipoEvento(evento.name());

        if (canal == EnumCanalNotificacion.EMAIL) {
            dto.setDestinatario(authUser.getEmail());
        } else {
            dto.setDestinatario(usuario.getTelefono());
        }

        enviarNotificacion(dto);
    }

    /**
     * Envia una notificacion segun el evento configurado en plantillas activas
     *
     * @param request Datos del evento y usuario destino
     */
    public void enviarNotificacionPorEvento(EnvioEventoNotificacionDTO request) {
        List<PlantillaNotificacion> plantillas = plantillaRepository
                .findByEventosAsociadosContainingAndEstadoTrueAndEliminadaFalse(request.getEvento());

        if (plantillas.isEmpty()) {
            throw new RuntimeException("No existe plantilla activa para el evento: " + request.getEvento());
        }

        PlantillaNotificacion plantilla = plantillas.get(0);
        enviarNotificacionConPlantilla(
                plantilla.getIdPlantilla(),
                request.getUsuarioId(),
                request.getVariablesAdicionales());
    }

    /**
     * Envia una notificacion por el canal indicado
     *
     * @param dto Datos del envio
     */
    public void enviarNotificacion(EnvioNotificacionDTO dto) {
        logger.info("Enviando notificacion a {} por canal {}", dto.getDestinatario(), dto.getCanal());

        EnumCanalNotificacion canal = EnumCanalNotificacion.valueOf(dto.getCanal().toUpperCase());
        EnumEventoAsociado evento = dto.getTipoEvento() != null
                ? EnumEventoAsociado.valueOf(dto.getTipoEvento())
                : EnumEventoAsociado.WELCOME;

        preferenciaUsuarioService.validarPreferenciasUsuario(dto.getUsuarioId(), evento, canal);
        rateLimitService.validarLimiteEnvio(dto.getUsuarioId());

        PlantillaNotificacion plantilla = null;
        if (dto.getPlantillaId() != null) {
            plantilla = plantillaRepository.findById(dto.getPlantillaId()).orElse(null);
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setId_usuario(dto.getUsuarioId());
        notificacion.setId_plantilla(plantilla);
        notificacion.setTitulo(dto.getAsunto() != null ? dto.getAsunto() : "Notificacion Pulse Gym");
        notificacion.setContenido(dto.getContenido());
        notificacion.setEstado(EnumEstadoNotificacion.PENDIENTE);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);

        try {
            if (canal == EnumCanalNotificacion.EMAIL) {
                // Construir contexto con variables del usuario para pasar al diseño del email
                Map<String, Object> contexto = construirContextoParaEmail(dto);
                emailService.enviarEmailHtml(
                        dto.getDestinatario(),
                        notificacion.getTitulo(),
                        dto.getContenido(), evento, contexto);
                notificacion.setEstado(EnumEstadoNotificacion.ENVIADO);
            } else {
                whatsAppService.enviarWhatsApp(dto.getDestinatario(), dto.getContenido());
                notificacion.setEstado(EnumEstadoNotificacion.ENVIADO);
            }

            notificacionRepository.save(notificacion);
            logger.info("Notificacion enviada exitosamente a {}", dto.getDestinatario());
        } catch (Exception e) {
            logger.error("Error al enviar notificacion: {}", e.getMessage());
            notificacion.setEstado(EnumEstadoNotificacion.RECHAZADO);
            notificacionRepository.save(notificacion);
            throw new RuntimeException("Error al enviar notificacion: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el usuario de auth por su identificador
     * @param usuarioAuthId Identificador del usuario en auth
     * @return Usuario de auth
     */
    private AuthUserDTO obtenerAuthUser(Long usuarioAuthId) {
        try {
            return authClient.obtenerUsuarioPorId(usuarioAuthId);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo obtener datos de auth para el usuario: " + usuarioAuthId);
        }
    }

    /**
     * Obtiene el perfil de un usuario por su email
     * @param email Email del usuario
     * @return Perfil del usuario
     */
    private UsuarioPerfilResponseDTO obtenerPerfilPorEmail(String email) {
        try {
            return usuarioClient.obtenerUsuarioPorEmail(email);
        } catch (Exception e) {
            logger.warn("No se pudo obtener perfil por email {}: {}", email, e.getMessage());
            UsuarioPerfilResponseDTO perfil = new UsuarioPerfilResponseDTO();
            perfil.setEmail(email);
            return perfil;
        }
    }

    /**
     * Resolve el evento asociado a una plantilla de notificacion
     * @param plantilla Plantilla de notificacion
     * @return Evento asociado a la plantilla
     */
    private EnumEventoAsociado resolverEventoPlantilla(PlantillaNotificacion plantilla) {
        if (plantilla.getEventosAsociados() != null && !plantilla.getEventosAsociados().isEmpty()) {
            return plantilla.getEventosAsociados().iterator().next();
        }
        return plantilla.getEventoAsociado();
    }

    private Map<String, Object> construirContexto(UsuarioPerfilResponseDTO usuario,
            AuthUserDTO authUser,
            Map<String, Object> variablesAdicionales) {

        Map<String, Object> contexto = new HashMap<>();

        if (usuario != null) {
            contexto.put("id", usuario.getIdUsuario());
            contexto.put("id_usuario", usuario.getIdUsuario());
            contexto.put("nombre", usuario.getNombre());
            contexto.put("nombre_usuario", usuario.getNombre());
            contexto.put("apellido", usuario.getApellido());
            contexto.put("apellidos", usuario.getApellido());
            contexto.put("telefono", usuario.getTelefono());
            contexto.put("documento", usuario.getDocumentoIdentidad());

            if (usuario.getFechaRegistro() != null) {
                contexto.put("fecha_registro", usuario.getFechaRegistro().format(DATE_FORMATTER));
            }

            if (usuario.getFechaNacimiento() != null) {
                contexto.put("fecha_nacimiento", usuario.getFechaNacimiento().format(DATE_FORMATTER));
            }

            if (usuario.getObjetivoPrincipal() != null) {
                contexto.put("objetivo", usuario.getObjetivoPrincipal());
            }

            if (usuario.getNivelExperiencia() != null) {
                contexto.put("nivel_experiencia", usuario.getNivelExperiencia().toString());
            }
        }

        if (authUser != null) {
            contexto.put("email", authUser.getEmail());
            contexto.put("username", authUser.getUsername());
            contexto.put("rol", authUser.getRol() != null ? authUser.getRol().toString() : null);
            contexto.put("estado_usuario", authUser.getEstado());
        }

        if (variablesAdicionales != null) {
            contexto.putAll(variablesAdicionales);
        }

        return contexto;
    }

    /**
     * Construye el contexto con variables del usuario para pasar al diseño del email.
     * Este contexto se usa para reemplazar variables en el header/footer del email.
     * @param dto Datos del envio
     * @return Mapa con variables para el diseño
     */
    private Map<String, Object> construirContextoParaEmail(EnvioNotificacionDTO dto) {
        Map<String, Object> contexto = new HashMap<>();
        
        // Agregar información básica disponible
        if (dto.getUsuarioId() != null) {
            contexto.put("usuario_id", dto.getUsuarioId());
        }
        
        // Agregar variables adicionales que vengan en el contenido
        // El contenido ya tiene las variables reemplazadas por el renderService,
        // pero pasamos el contexto completo por si el diseño necesita algo más
        if (dto.getContenido() != null) {
            // Extraer variables del contenido si es necesario
        }
        
        return contexto;
    }
}
