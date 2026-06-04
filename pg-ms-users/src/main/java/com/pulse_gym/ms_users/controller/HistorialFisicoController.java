package com.pulse_gym.ms_users.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pulse_gym.lb_common.dto.HistorialFisicoRequestDTO;
import com.pulse_gym.lb_common.dto.HistorialFisicoResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.ms_users.service.HistorialFisicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios/historial-fisico")
@RequiredArgsConstructor
public class HistorialFisicoController {

    /** Servicio del historial físico */
    private final HistorialFisicoService historialService;

    /**
     * Registra una nueva medición física para un socio
     * 
     * @param historial Medición física a registrar
     * @return Mensaje de éxito si la medición se registró correctamente
     */
    @PostMapping
    public ResponseEntity<MessegeGlobalDTO> registrarMedicion(
            @Valid @RequestBody HistorialFisicoRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {
        try {
            MessegeGlobalDTO response = historialService.registrarMedicion(requestDTO, userRol, userIdAutenticado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar medición", e);
        }
    }

    /**
     * Consulta el historial físico de un socio
     * 
     * @param idSocio           ID del socio
     * @param userRol           Rol del usuario autenticado
     * @param userIdAutenticado ID del usuario autenticado
     * @return Lista de mediciones físicas del socio
     */
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<List<HistorialFisicoResponseDTO>> consultarHistorial(
            @PathVariable Long idSocio,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {
        try {
            List<HistorialFisicoResponseDTO> historial = historialService.consultarHistorial(idSocio, userRol, userIdAutenticado);
            return ResponseEntity.ok(historial);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar historial", e);
        }
    }
}
