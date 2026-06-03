package com.pulse_gym.ms_notifications.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.EnvioEventoNotificacionDTO;
import com.pulse_gym.lb_common.dto.EnvioNotificacionDTO;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_notifications.services.NotificacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notificaciones")
public class NotificacionEnvioController {

    private final NotificacionService notificacionService;

    /**
     * Envia una notificacion manual por canal
     *
     * @param request Datos del envio
     * @param userRol Rol del usuario autenticado
     * @return Resultado del envio
     */
    @PostMapping("/enviar")
    public ResponseEntity<Map<String, Object>> enviarNotificacion(
            @Valid @RequestBody EnvioNotificacionDTO request,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        try {
            ValidacionDeRoles.validarAdmin(userRol);
            notificacionService.enviarNotificacion(request);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", "Notificacion enviada exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Envia una notificacion usando plantilla y variables dinamicas
     *
     * @param plantillaId          Identificador de la plantilla
     * @param usuarioId            Identificador del usuario en auth
     * @param variablesAdicionales Variables adicionales
     * @param userRol              Rol del usuario autenticado
     * @return Resultado del envio
     */
    @PostMapping("/enviar-plantilla/{plantillaId}/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> enviarConPlantilla(
            @PathVariable Long plantillaId,
            @PathVariable Long usuarioId,
            @RequestBody(required = false) Map<String, Object> variablesAdicionales,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        try {
            ValidacionDeRoles.validarAdmin(userRol);
            notificacionService.enviarNotificacionConPlantilla(
                    plantillaId,
                    usuarioId,
                    variablesAdicionales != null ? variablesAdicionales : new HashMap<>());

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", "Notificacion con plantilla enviada exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Envia una notificacion segun el evento configurado en plantillas activas
     *
     * @param request Datos del evento
     * @param userRol Rol del usuario autenticado
     * @return Resultado del envio
     */
    @PostMapping("/enviar-evento")
    public ResponseEntity<Map<String, Object>> enviarPorEvento(
            @Valid @RequestBody EnvioEventoNotificacionDTO request,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        try {
            ValidacionDeRoles.validarAdmin(userRol);
            notificacionService.enviarNotificacionPorEvento(request);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", "Notificacion enviada por evento");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
