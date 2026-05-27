package com.pulse_gym.ms_operation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.pulse_gym.lb_common.dto.AsistenciaResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RegistroAsistenciaDTO;
import com.pulse_gym.ms_operation.services.AsistenciaService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    /**
     * Registrar entrada de socio (desde WEB o APP)
     * POST /api/asistencias/entrada
     */
    @PostMapping("/entrada")
    public ResponseEntity<Map<String, Object>> registrarEntrada(@Valid @RequestBody RegistroAsistenciaDTO request, @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            MessegeGlobalDTO response = asistenciaService.registrarEntrada(request);
            
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
     * Consultar historial de asistencias de un socio
     * GET /api/asistencias/historial/usuario/{idUsuario}
     */
    @GetMapping("/historial/usuario/{idUsuario}")
    public ResponseEntity<Map<String, Object>> consultarHistorialUsuario(@PathVariable Long idUsuario) {
        try {
            List<AsistenciaResponseDTO> historial = asistenciaService.consultarHistorialUsuario(idUsuario);
            
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
     * Consultar asistencias por sede
     * GET /api/asistencias/sede/{idSede}
     */
    @GetMapping("/sede/{idSede}")
    public ResponseEntity<Map<String, Object>> consultarAsistenciasPorSede(@PathVariable Long idSede) {
        try {
            List<AsistenciaResponseDTO> asistencias = asistenciaService.consultarAsistenciasPorSede(idSede);
            
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
     * Consultar asistencias del día actual
     * GET /api/asistencias/hoy
     */
    @GetMapping("/hoy")
    public ResponseEntity<Map<String, Object>> consultarAsistenciasDelDia() {
        try {
            List<AsistenciaResponseDTO> asistencias = asistenciaService.consultarAsistenciasDelDia();
            
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
}