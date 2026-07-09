package com.pulse_gym.ms_operation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulse_gym.lb_common.dto.AsistenciaResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RegistroAsistenciaBiometricaDTO;
import com.pulse_gym.lb_common.dto.RegistroAsistenciaDTO;
import com.pulse_gym.ms_operation.services.AsistenciaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    /**
     * Inyeccion de AsistenciaService para manejar las operaciones de base de datos
     * relacionadas con las asistencias
     */
    private final AsistenciaService asistenciaService;

    /**
     * Registrar entrada de socio (desde WEB o APP)
     * POST /api/asistencias/entrada
     * 
     * Se valida que la peticion solo la puede hacer un socio
     * 
     * @param request
     * @param userRol Rol del usuario que hace la petición (desde header X-User-Rol)
     * @return ResponseEntity<Map<String, Object>> con el resultado de la operación
     * 
     */
    @PostMapping("/entrada")
    public ResponseEntity<Map<String, Object>> registrarEntrada(@Valid @RequestBody RegistroAsistenciaDTO request,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            MessegeGlobalDTO response = asistenciaService.registrarEntrada(request, userRol);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", response.getMessage());

            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * Consultar historial de asistencias de un socio.
     * 
     * @param idUsuario ID del socio
     * @param userRol   Rol del socio
     * @return ResponseEntity<Map<String, Object>> con el resultado de la operación
     */

    @GetMapping("/historial/usuario/{idUsuario}")
    public ResponseEntity<Map<String, Object>> consultarHistorialUsuario(@PathVariable Long idUsuario,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            List<AsistenciaResponseDTO> historial = asistenciaService.consultarHistorialUsuario(idUsuario, userRol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Historial de asistencias encontrado");
            response.put("count", historial.size());
            response.put("data", historial);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Consultar asistencias por sede.
     * 
     * @param idSede  ID de la sede
     * @param userRol Rol del socio
     * @return ResponseEntity<Map<String, Object>> con el resultado de la operación
     */
    @GetMapping("/sede/{idSede}")
    public ResponseEntity<Map<String, Object>> consultarAsistenciasPorSede(@PathVariable Long idSede,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            List<AsistenciaResponseDTO> asistencias = asistenciaService.consultarAsistenciasPorSede(idSede, userRol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Asistencias encontradas para la sede");
            response.put("count", asistencias.size());
            response.put("data", asistencias);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    /**
     * Consultar asistencias del día actual.
     * 
     * @param userRol Rol del usuario
     * @return ResponseEntity<Map<String, Object>> con el resultado de la operación
     */
    @GetMapping("/hoy")
    public ResponseEntity<Map<String, Object>> consultarAsistenciasDelDia(
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            List<AsistenciaResponseDTO> asistencias = asistenciaService.consultarAsistenciasDelDia(userRol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Asistencias registradas hoy");
            response.put("count", asistencias.size());
            response.put("data", asistencias);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/entrada-biometrica")
    public ResponseEntity<Map<String, Object>> registrarEntradaBiometrica(
            @Valid @RequestBody RegistroAsistenciaBiometricaDTO request) {
        try {
            MessegeGlobalDTO response = asistenciaService.registrarEntradaBiometrica(request);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", response.getMessage());

            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

}