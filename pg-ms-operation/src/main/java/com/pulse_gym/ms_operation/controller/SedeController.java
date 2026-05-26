// SedeController.java
package com.pulse_gym.ms_operation.controller;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.SedeRequestDTO;
import com.pulse_gym.lb_common.dto.SedeResponseDTO;
import com.pulse_gym.lb_common.dto.SedeUpdateDTO;
import com.pulse_gym.ms_operation.services.SedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
public class SedeController {

    /**
     * Inyeccion de SedeService para manejar la lógica de negocio relacionada con
     * las sedes, como el registro y la obtención de sedes.
     */
    private final SedeService sedeService;

    /**
     * Endpoint para registrar una nueva sede. Recibe un objeto SedeRequestDTO en
     * el cuerpo de la solicitud,
     * 
     * @param request
     * @return ResponseEntity<Map<String, Object>> con la respuesta de la creación
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearSede(@Valid @RequestBody SedeRequestDTO request) {
        try {
            MessegeGlobalDTO response = sedeService.crearSede(request);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("success", true);
            respuesta.put("message", response.getMessage());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> obtenerTodasLasSedes() {
        try {
            List<SedeResponseDTO> sedes = sedeService.obtenerTodasLasSedes();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Consulta exitosa");
            response.put("count", sedes.size());
            response.put("data", sedes);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> obtenerSedePorId(@PathVariable Long id) {
        try {
            SedeResponseDTO sede = sedeService.obtenerSedePorId(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sede encontrada");
            response.put("data", sede);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizarSede(
            @PathVariable Long id,
            @Valid @RequestBody SedeUpdateDTO request) {
        try {
            MessegeGlobalDTO response = sedeService.actualizarSede(id, request);
            
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminarSede(@PathVariable Long id) {
        try {
            MessegeGlobalDTO response = sedeService.eliminarSede(id);
            
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

    @GetMapping("/buscar/nombre")
    public ResponseEntity<Map<String, Object>> buscarSedesPorNombre(@RequestParam String nombre) {
        try {
            List<SedeResponseDTO> sedes = sedeService.buscarSedesPorNombre(nombre);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sedes encontradas");
            response.put("count", sedes.size());
            response.put("data", sedes);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/buscar/ciudad")
    public ResponseEntity<Map<String, Object>> buscarSedesPorCiudad(@RequestParam String ciudad) {
        try {
            List<SedeResponseDTO> sedes = sedeService.buscarSedesPorCiudad(ciudad);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sedes encontradas");
            response.put("count", sedes.size());
            response.put("data", sedes);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/paginado")
    public ResponseEntity<Map<String, Object>> obtenerSedesPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<SedeResponseDTO> sedesPage = sedeService.obtenerSedesPaginado(page, size);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Consulta exitosa");
            response.put("currentPage", sedesPage.getNumber());
            response.put("totalItems", sedesPage.getTotalElements());
            response.put("totalPages", sedesPage.getTotalPages());
            response.put("pageSize", sedesPage.getSize());
            response.put("data", sedesPage.getContent());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}