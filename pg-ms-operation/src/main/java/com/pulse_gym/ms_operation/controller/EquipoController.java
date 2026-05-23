package com.pulse_gym.ms_operation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.EquipoRequestDTO;
import com.pulse_gym.lb_common.dto.EquipoResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.HttpGlobalResponse;
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

    @GetMapping
    public ResponseEntity<HttpGlobalResponse<EquipoResponseDTO>> obtenerEquipos() {
        try {
            HttpGlobalResponse<EquipoResponseDTO> response = equipoService.obtenerEquipos();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();

            HttpGlobalResponse<EquipoResponseDTO> dto = new HttpGlobalResponse<>();
            dto.setMessage(e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(dto);
        }
    }

}
