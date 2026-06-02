package com.pulse_gym.ms_notifications.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.ms_notifications.services.NotificacionService;
import com.pulse_gym.lb_common.dto.EnvioNotificacionDTO;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notificaciones")
public class NotificacionEnvioController {
    
    /**
     * Inyeccion de NotificacionService para manejar las operaciones de base de datos
     * relacionadas con las notificaciones
     */
    private final NotificacionService notificacionService;
    
    /**
     * Endpoint para enviar una notificación a un usuario específico o a un grupo de usuarios.
     * @param request     Objeto con los datos necesarios para enviar la notificación
     * @param userRol     Rol del usuario que hace la peticion (desde header X-User-Rol)
     * @return ResponseEntity<Map<String, Object>> con el resultado de la operación
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
            respuesta.put("message", "Notificación enviada exitosamente");
            
            return ResponseEntity.ok(respuesta);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}