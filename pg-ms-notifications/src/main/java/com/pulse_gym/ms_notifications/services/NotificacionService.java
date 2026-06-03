package com.pulse_gym.ms_notifications.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.client.AuthClient;
import com.pulse_gym.lb_common.client.UsuarioClient;
import com.pulse_gym.lb_common.dto.AuthUserDTO;
import com.pulse_gym.lb_common.dto.EnvioNotificacionDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.notification.Notificacion;
import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;
import com.pulse_gym.lb_common.enums.EnumEstadoNotificacion;
import com.pulse_gym.ms_notifications.repository.NotificacionRepository;
import com.pulse_gym.ms_notifications.repository.PlantillaNotificationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class NotificacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    @Autowired
    private PlantillaNotificationRepository plantillaRepository;
    
    @Autowired
    private PlantillaRenderService renderService;
    
    @Autowired
    private UsuarioClient usuarioClient;
    
    @Autowired
    private AuthClient authClient;  // ← Cliente para auth
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    /**
     * Envía una notificación usando plantilla con variables dinámicas
     */
    public void enviarNotificacionConPlantilla(Long plantillaId, Long usuarioId, 
                                                Map<String, Object> variablesAdicionales) {
        
        PlantillaNotificacion plantilla = plantillaRepository.findById(plantillaId)
            .orElseThrow(() -> new RuntimeException("Plantilla no encontrada con id: " + plantillaId));
        
        // Obtener datos del perfil desde ms-users
        UsuarioPerfilResponseDTO usuario = null;
        try {
            usuario = usuarioClient.obtenerUsuarioPorId(usuarioId);
        } catch (Exception e) {
            logger.warn("No se pudo obtener perfil de usuario desde ms-users: {}", e.getMessage());
            usuario = new UsuarioPerfilResponseDTO();
            usuario.setIdUsuario(usuarioId);
        }
        
        // Obtener email y datos de autenticación desde ms-auth
        AuthUserDTO authUser = null;
        try {
            authUser = authClient.obtenerUsuarioPorId(usuarioId);
        } catch (Exception e) {
            logger.error("No se pudo obtener datos de auth para usuario {}: {}", usuarioId, e.getMessage());
            throw new RuntimeException("No se pudo obtener email del usuario. Asegúrate que ms-auth esté corriendo");
        }
        
        if (authUser == null || authUser.getEmail() == null) {
            throw new RuntimeException("Email no encontrado para usuario: " + usuarioId);
        }
        
        // Construir contexto combinando datos de ambos servicios
        Map<String, Object> contexto = construirContexto(usuario, authUser, variablesAdicionales);
        
        logger.info("Variables encontradas en plantilla: {}", renderService.extraerVariables(plantilla.getContenido()));
        
        String contenidoRenderizado = renderService.renderizar(plantilla.getContenido(), contexto);
        
        EnvioNotificacionDTO dto = new EnvioNotificacionDTO();
        dto.setUsuarioId(usuarioId);
        dto.setDestinatario(authUser.getEmail());  // Email desde auth
        dto.setContenido(contenidoRenderizado);
        dto.setCanal("EMAIL");
        dto.setPlantillaId(plantillaId);
        if (plantilla.getEventoAsociado() != null) {
            dto.setTipoEvento(plantilla.getEventoAsociado().toString());
        }
        
        enviarNotificacion(dto);
        
        logger.info("Notificación con plantilla '{}' enviada a usuario {} ({})", 
            plantilla.getNombre(), usuarioId, authUser.getEmail());
    }
    
    /**
     * Construye el contexto con datos de usuario y auth
     */
    private Map<String, Object> construirContexto(UsuarioPerfilResponseDTO usuario, 
                                                    AuthUserDTO authUser,
                                                    Map<String, Object> variablesAdicionales) {
        Map<String, Object> contexto = new HashMap<>();
        
        // Datos desde ms-users
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
        
        // Datos desde ms-auth
        if (authUser != null) {
            contexto.put("email", authUser.getEmail());
            contexto.put("username", authUser.getUsername());
            contexto.put("rol", authUser.getRol() != null ? authUser.getRol().toString() : null);
            contexto.put("estado_usuario", authUser.getEstado());
        }
        
        // Variables adicionales
        if (variablesAdicionales != null) {
            for (Map.Entry<String, Object> entry : variablesAdicionales.entrySet()) {
                contexto.put(entry.getKey(), entry.getValue());
            }
        }
        
        return contexto;
    }
    
    // Tu método enviarNotificacion original se mantiene igual
    public void enviarNotificacion(EnvioNotificacionDTO dto) {
        logger.info("Enviando notificación a {} por canal {}", dto.getDestinatario(), dto.getCanal());
        
        PlantillaNotificacion plantilla = null;
        if (dto.getPlantillaId() != null) {
            plantilla = plantillaRepository.findById(dto.getPlantillaId()).orElse(null);
        }
        
        Notificacion notificacion = new Notificacion();
        notificacion.setId_usuario(dto.getUsuarioId());
        notificacion.setId_plantilla(plantilla);
        notificacion.setTitulo(dto.getAsunto() != null ? dto.getAsunto() : "Notificación Pulse Gym");
        notificacion.setContenido(dto.getContenido());
        notificacion.setEstado(EnumEstadoNotificacion.PENDIENTE);
        notificacion.setFechaEnvio(LocalDateTime.now());
        
        notificacionRepository.save(notificacion);
        
        try {
            if ("EMAIL".equalsIgnoreCase(dto.getCanal())) {
                emailService.enviarEmail(
                    dto.getDestinatario(),
                    dto.getAsunto() != null ? dto.getAsunto() : "Notificación Pulse Gym",
                    dto.getContenido()
                );
                notificacion.setEstado(EnumEstadoNotificacion.ENVIADO);
            } else if ("WHATSAPP".equalsIgnoreCase(dto.getCanal())) {
                logger.warn("WhatsApp aún no implementado");
                notificacion.setEstado(EnumEstadoNotificacion.RECHAZADO);
                throw new RuntimeException("WhatsApp no implementado");
            }
            
            notificacionRepository.save(notificacion);
            logger.info("Notificación enviada exitosamente a {}", dto.getDestinatario());
            
        } catch (Exception e) {
            logger.error("Error al enviar notificación: {}", e.getMessage());
            notificacion.setEstado(EnumEstadoNotificacion.RECHAZADO);
            notificacionRepository.save(notificacion);
            throw new RuntimeException("Error al enviar notificación: " + e.getMessage(), e);
        }
    }
}