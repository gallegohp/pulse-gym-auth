package com.pulse_gym.ms_users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pulse_gym.lb_common.dto.CertificacionRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.ms_users.service.CertificacionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios/certificaciones")
@RequiredArgsConstructor
public class CertificacionController {

    /** Servicio para gestionar las certificaciones */
    private final CertificacionService certificacionService;

    /**
     * Endpoint para registrar una nueva certificación para un entrenador específico.
     * @param requestDTO DTO con los datos de la certificación a registrar
     * @param userRol Rol del usuario que realiza la acción (obtenido del token de autenticación)
     * @return Mensaje de éxito o error en el registro de la certificación
     */
    @PostMapping
    public ResponseEntity<MessegeGlobalDTO> registrarCertificacion(
            @Valid @RequestBody CertificacionRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            MessegeGlobalDTO response = certificacionService.registrarCertificacion(requestDTO, userRol);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SecurityAuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar la certificación");
        }
    }
}
