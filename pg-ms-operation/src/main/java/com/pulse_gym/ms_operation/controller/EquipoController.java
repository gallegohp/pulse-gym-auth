package com.pulse_gym.ms_operation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.ConsultaEquipoRequestDTO;
import com.pulse_gym.lb_common.dto.EquipoRequestDTO;
import com.pulse_gym.lb_common.dto.EstadoEquipoRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.operation.Equipo;
import com.pulse_gym.ms_operation.services.EquipoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/equipos")
@RequiredArgsConstructor
public class EquipoController {

    /**
     * Inyeccion de EquipoService para manejar la lógica de negocio relacionada con los equipos, como el registro y la obtención de equipos. 
     */
    private final EquipoService equipoService;

    /**
     * Endpoint para registrar un nuevo equipo. Recibe un objeto EquipoRequestDTO en el cuerpo de la solicitud, 
     * @param equipoRequestDTO
     * @return ResponseEntity<MessegeGlobalDTO> 
     */
    @PostMapping
    public ResponseEntity<MessegeGlobalDTO> registrarEquipo(@Valid @RequestBody EquipoRequestDTO equipoRequestDTO) {
        try {
            MessegeGlobalDTO response = equipoService.registrarEquipo(equipoRequestDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            e.printStackTrace();

            MessegeGlobalDTO dto = new MessegeGlobalDTO(e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(dto);
        }
    }

    @PostMapping("/consultar")
    public ResponseEntity<Map<String, Object>> consultarEquipos(@RequestBody ConsultaEquipoRequestDTO request) {
        try {
            List<Equipo> equipos = equipoService.obtenerEquipos(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            
            // Mensaje diferenciado según si hay resultados o no
            if (equipos.isEmpty()) {
                response.put("message", "Consulta exitosa, no se encontraron equipos");
                response.put("count", 0);
                response.put("data", equipos);
            } else {
                response.put("message", "Consulta exitosa");
                response.put("count", equipos.size());
                response.put("data", equipos);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error al consultar equipos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessegeGlobalDTO> actualizarEquipo(@PathVariable Long id, @Valid @RequestBody EquipoRequestDTO equipoRequestDTO) {
        try {
            MessegeGlobalDTO response = equipoService.actualizarEquipo(id, equipoRequestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            MessegeGlobalDTO dto = new MessegeGlobalDTO(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
        }
    }

    @PatchMapping("/{id}/estado")
public ResponseEntity<MessegeGlobalDTO> cambiarEstadoEquipo(
        @PathVariable Long id, 
        @Valid @RequestBody EstadoEquipoRequestDTO estadoRequestDTO) {
    try {
        MessegeGlobalDTO response = equipoService.cambiarEstadoEquipo(id, estadoRequestDTO);
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        e.printStackTrace();
        MessegeGlobalDTO dto = new MessegeGlobalDTO(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }
}

}
