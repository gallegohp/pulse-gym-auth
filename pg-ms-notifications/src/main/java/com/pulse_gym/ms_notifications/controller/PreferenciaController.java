package com.pulse_gym.ms_notifications.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.PreferenciaUsuarioRequestDTO;
import com.pulse_gym.lb_common.dto.PreferenciaUsuarioResponseDTO;
import com.pulse_gym.ms_notifications.services.PreferenciaUsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/preferencias")
public class PreferenciaController {

    /**
     * Inyeccion de servicio de preferencias de usuario
     */
    private final PreferenciaUsuarioService preferenciaUsuarioService;

    /**
     * Obtiene las preferencias del socio autenticado
     *
     * @param usuarioId Identificador del usuario en auth
     * @param userRol   Rol del usuario autenticado
     * @return Preferencias del usuario
     */
    @GetMapping("/mis-preferencias")
    public ResponseEntity<Map<String, Object>> obtenerMisPreferencias(
            @RequestHeader(value = "X-User-Id", required = false) Long usuarioId,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        PreferenciaUsuarioResponseDTO data = preferenciaUsuarioService.obtenerMisPreferencias(usuarioId, userRol);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza las preferencias del socio autenticado
     *
     * @param usuarioId Identificador del usuario en auth
     * @param request   Datos de preferencias
     * @param userRol   Rol del usuario autenticado
     * @return Preferencias actualizadas
     */
    @PutMapping("/mis-preferencias")
    public ResponseEntity<Map<String, Object>> actualizarMisPreferencias(
            @RequestHeader(value = "X-User-Id", required = false) Long usuarioId,
            @Valid @RequestBody PreferenciaUsuarioRequestDTO request,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        PreferenciaUsuarioResponseDTO data = preferenciaUsuarioService.actualizarMisPreferencias(
                usuarioId, request, userRol);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Preferencias actualizadas correctamente");
        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}
