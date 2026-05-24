package com.pulse_gym.ms_operation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.dto.MantenimientoRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.ms_operation.services.MantenimientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mantenimientos")
@RequiredArgsConstructor
public class MantenimientoController {
    
    private final MantenimientoService mantenimientoService;

    @PostMapping
    public ResponseEntity<MessegeGlobalDTO> registrarMantenimiento(@Valid @RequestBody MantenimientoRequestDTO mantenimientoRequestDTO) {
        try {
            MessegeGlobalDTO response = mantenimientoService.registrarMantenimiento(mantenimientoRequestDTO);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            e.printStackTrace();

            MessegeGlobalDTO dto = new MessegeGlobalDTO(e.getMessage());

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(dto);
        }
    }
}
