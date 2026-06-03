package com.pulse_gym.ms_notifications.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.PreferenciaUsuarioRequestDTO;
import com.pulse_gym.lb_common.dto.PreferenciaUsuarioResponseDTO;
import com.pulse_gym.lb_common.dto.VerificarPreferenciaRequestDTO;
import com.pulse_gym.lb_common.dto.VerificarPreferenciaResponseDTO;
import com.pulse_gym.lb_common.entity.notification.PreferenciaUsuario;
import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;
import com.pulse_gym.lb_common.enums.EnumPreferenciaUsuario;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_notifications.repository.PreferenciaUsuarioRepository;
import com.pulse_gym.ms_notifications.util.EventoNotificacionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenciaUsuarioService {

    private final PreferenciaUsuarioRepository preferenciaUsuarioRepository;
    private final RateLimitService rateLimitService;

    /**
     * Obtiene las preferencias del usuario autenticado
     *
     * @param usuarioId Identificador del usuario en auth
     * @param userRol   Rol del usuario autenticado
     * @return Preferencias del usuario
     */
    @Transactional(readOnly = true)
    public PreferenciaUsuarioResponseDTO obtenerMisPreferencias(Long usuarioId, String userRol) {
        ValidacionDeRoles.validarSocio(userRol);
        return mapearAResponse(obtenerOPreferenciasPorDefecto(usuarioId));
    }

    /**
     * Actualiza las preferencias del usuario autenticado
     *
     * @param usuarioId Identificador del usuario en auth
     * @param request   Datos de preferencias
     * @param userRol   Rol del usuario autenticado
     * @return Preferencias actualizadas
     */
    @Transactional
    public PreferenciaUsuarioResponseDTO actualizarMisPreferencias(
            Long usuarioId, PreferenciaUsuarioRequestDTO request, String userRol) {

        ValidacionDeRoles.validarSocio(userRol);

        PreferenciaUsuario preferencia = preferenciaUsuarioRepository.findByIdUsuario(usuarioId)
                .orElseGet(() -> crearPreferenciaPorDefecto(usuarioId));

        preferencia.setPreferencia(request.getPreferencia());
        preferencia.setLogros_habilitado(request.getLogrosHabilitado());
        preferencia.setMantenimientos_habilitado(request.getMantenimientosHabilitado());
        preferencia.setPromociones_habilitado(request.getPromocionesHabilitado());

        preferenciaUsuarioRepository.save(preferencia);
        return mapearAResponse(preferencia);
    }

    /**
     * Verifica si un envio esta permitido segun preferencias y limites
     *
     * @param request Datos de verificacion
     * @return Resultado de la verificacion
     */
    @Transactional(readOnly = true)
    public VerificarPreferenciaResponseDTO verificarEnvioPermitido(VerificarPreferenciaRequestDTO request) {
        VerificarPreferenciaResponseDTO response = new VerificarPreferenciaResponseDTO();

        try {
            validarPreferenciasUsuario(
                    request.getUsuarioId(),
                    request.getTipoEvento(),
                    request.getCanal());
            rateLimitService.validarLimiteEnvio(request.getUsuarioId());
            response.setPermitido(true);
            response.setMotivo("Envio permitido");
        } catch (RuntimeException ex) {
            response.setPermitido(false);
            response.setMotivo(ex.getMessage());
        }

        return response;
    }

    /**
     * Valida preferencias de canal y categoria antes de un envio
     *
     * @param usuarioId   Identificador del usuario en auth
     * @param tipoEvento  Evento de la notificacion
     * @param canal       Canal de envio
     */
    public void validarPreferenciasUsuario(Long usuarioId, EnumEventoAsociado tipoEvento, EnumCanalNotificacion canal) {
        PreferenciaUsuario preferencia = obtenerOPreferenciasPorDefecto(usuarioId);

        if (!EventoNotificacionUtil.canalHabilitado(preferencia.getPreferencia(), canal)) {
            throw new RuntimeException("El usuario no acepta notificaciones por el canal solicitado");
        }

        if (EventoNotificacionUtil.esLogro(tipoEvento) && Boolean.FALSE.equals(preferencia.getLogros_habilitado())) {
            throw new RuntimeException("El usuario deshabilito notificaciones de logros");
        }

        if (EventoNotificacionUtil.esMantenimiento(tipoEvento)
                && Boolean.FALSE.equals(preferencia.getMantenimientos_habilitado())) {
            throw new RuntimeException("El usuario deshabilito notificaciones de mantenimiento");
        }

        if (EventoNotificacionUtil.esPromocion(tipoEvento)
                && Boolean.FALSE.equals(preferencia.getPromociones_habilitado())) {
            throw new RuntimeException("El usuario deshabilito notificaciones promocionales");
        }
    }

    private PreferenciaUsuario obtenerOPreferenciasPorDefecto(Long usuarioId) {
        return preferenciaUsuarioRepository.findByIdUsuario(usuarioId)
                .orElseGet(() -> crearPreferenciaPorDefecto(usuarioId));
    }

    private PreferenciaUsuario crearPreferenciaPorDefecto(Long usuarioId) {
        PreferenciaUsuario preferencia = new PreferenciaUsuario();
        preferencia.setIdUsuario(usuarioId);
        preferencia.setPreferencia(EnumPreferenciaUsuario.AMBOS);
        preferencia.setLogros_habilitado(true);
        preferencia.setMantenimientos_habilitado(true);
        preferencia.setPromociones_habilitado(true);
        return preferenciaUsuarioRepository.save(preferencia);
    }

    private PreferenciaUsuarioResponseDTO mapearAResponse(PreferenciaUsuario preferencia) {
        PreferenciaUsuarioResponseDTO dto = new PreferenciaUsuarioResponseDTO();
        dto.setIdUsuario(preferencia.getIdUsuario());
        dto.setPreferencia(preferencia.getPreferencia());
        dto.setLogrosHabilitado(preferencia.getLogros_habilitado());
        dto.setMantenimientosHabilitado(preferencia.getMantenimientos_habilitado());
        dto.setPromocionesHabilitado(preferencia.getPromociones_habilitado());
        return dto;
    }
}
