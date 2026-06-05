package com.pulse_gym.ms_notifications.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.EnvioEventoNotificacionDTO;
import com.pulse_gym.lb_common.dto.VerificarPreferenciaRequestDTO;
import com.pulse_gym.lb_common.dto.VerificarPreferenciaResponseDTO;
import com.pulse_gym.ms_notifications.services.NotificacionService;
import com.pulse_gym.ms_notifications.services.PreferenciaUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internal/notificaciones")
public class NotificacionInternoController {

    private final PreferenciaUsuarioService preferenciaUsuarioService;
    private final NotificacionService notificacionService;

    /**
     * Verifica si un usuario acepta recibir una notificacion
     *
     * @param request Datos de verificacion
     * @return Resultado de la verificacion
     */
    @PostMapping("/verificar")
    public ResponseEntity<VerificarPreferenciaResponseDTO> verificarPreferencia(
            @Valid @RequestBody VerificarPreferenciaRequestDTO request) {
        return ResponseEntity.ok(preferenciaUsuarioService.verificarEnvioPermitido(request));
    }

    /**
     * Envia una notificacion automatica segun evento para otros microservicios
     *
     * @param request Datos del envio por evento
     * @return Resultado del envio
     */
    @PostMapping("/enviar-evento")
    public ResponseEntity<Map<String, Object>> enviarPorEvento(
            @Valid @RequestBody EnvioEventoNotificacionDTO request) {

        notificacionService.enviarNotificacionPorEvento(request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Notificacion enviada por evento");
        return ResponseEntity.ok(response);
    }
}
