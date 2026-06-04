package com.pulse_gym.lb_common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pulse_gym.lb_common.dto.EnvioEventoNotificacionDTO;
import com.pulse_gym.lb_common.dto.VerificarPreferenciaRequestDTO;
import com.pulse_gym.lb_common.dto.VerificarPreferenciaResponseDTO;

import java.util.Map;

@FeignClient(name = "pg-ms-notifications", url = "${microservicio.notificaciones.url:http://pg-ms-notifications:8083}")
public interface NotificacionClient {

    /**
     * Verifica si un usuario acepta recibir una notificacion segun sus preferencias
     * 
     * @param request Datos de verificacion (usuarioId, tipoEvento, canal)
     * @return Resultado indicando si el envio esta permitido y el motivo
     */
    @PostMapping("/api/internal/notificaciones/verificar")
    VerificarPreferenciaResponseDTO verificarPreferencia(@RequestBody VerificarPreferenciaRequestDTO request);

    /**
     * Envia una notificacion automatica segun evento desde otro microservicio
     * 
     * @param request Datos del envio por evento (usuarioId, evento, variablesAdicionales)
     * @return Resultado del envio
     */
    @PostMapping("/api/internal/notificaciones/enviar-evento")
    Map<String, Object> enviarPorEvento(@RequestBody EnvioEventoNotificacionDTO request);
}
