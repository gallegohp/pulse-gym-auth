package com.pulse_gym.ms_users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RegistrarPagoRequestDTO;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.ms_users.service.PagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    /** Servicio para operaciones con pagos */
    private final PagoService pagoService;

    /**
     * Endpoint para registrar un nuevo pago de una membresía.
     * 
     * @param requestDTO        DTO con los datos del pago (idSocioMembresia, monto,
     *                          metodoPago, etc.)
     * @param userRol           Rol del usuario autenticado - header "X-User-Rol"
     * @param userIdAutenticado ID del usuario que registra el pago - header
     *                          "X-User-Id"
     * @return Mensaje de confirmación con código HTTP 201 (Created)
     */
    @PostMapping("/registrar")
    public ResponseEntity<MessegeGlobalDTO> registrarPago(
            @Valid @RequestBody RegistrarPagoRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {
        try {
            MessegeGlobalDTO response = pagoService.registrarPago(requestDTO, userRol, userIdAutenticado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al registrar pago", e);
        }
    }

    /**
     * Endpoint para que un socio realice un pago desde la aplicación móvil.
     * 
     * @param requestDTO        DTO con los datos del pago (idSocioMembresia, monto,
     *                          metodoPago, etc.)
     * @param userRol           Rol del usuario autenticado - header "X-User-Rol"
     *                          (debe ser socio)
     * @param userIdAutenticado ID del usuario autenticado - header "X-User-Id"
     * @param userEmail         Email del socio autenticado - header "X-User-Email"
     * @return Mensaje de confirmación del pago con código HTTP 201 (Created)
     */
    @PostMapping("/pago-app")
    public ResponseEntity<MessegeGlobalDTO> realizarPagoApp(
            @Valid @RequestBody RegistrarPagoRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado,
            @RequestHeader(value = "X-User-Email", required = false) String userEmail) { // ← NUEVO
        try {
            MessegeGlobalDTO response = pagoService.realizarPagoApp(requestDTO, userRol, userIdAutenticado, userEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar pago desde app", e);
        }
    }
}
