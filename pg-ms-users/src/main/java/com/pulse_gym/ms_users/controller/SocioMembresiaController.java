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

import com.pulse_gym.lb_common.dto.AsignarMembresiaRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.SocioMembresiaResponseDTO;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.ms_users.service.SocioMembresiaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/socios-membresias")
@RequiredArgsConstructor
public class SocioMembresiaController {

    /** El servicio de socio membresia */
    private final SocioMembresiaService socioMembresiaService;

    /**
     * Endpoint para asignar una membresía a un socio. Recibe un DTO con los datos
     * necesarios para la asignación y el rol del usuario que realiza la solicitud.
     * 
     * @param requestDTO DTO con los datos necesarios para asignar la membresía al
     *                   socio
     * @param userRol    Rol del usuario que realiza la solicitud, obtenido del
     *                   encabezado "X-User-Rol"
     * @return ResponseEntity con un mensaje global indicando el resultado de la
     *         operación y el código de estado HTTP correspondiente
     */
    @PostMapping("/asignar")
    public ResponseEntity<MessegeGlobalDTO> asignarMembresia(
            @Valid @RequestBody AsignarMembresiaRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            MessegeGlobalDTO response = socioMembresiaService.asignarMembresia(requestDTO, userRol);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al asignar membresía", e);
        }
    }

    /**
     * Endpoint para consultar todas las membresías de un socio.
     * 
     * @param idSocio           ID del socio a consultar (viene en la URL)
     * @param userRol           Rol del usuario autenticado (socio, administrador o
     *                          recepcionista) - header "X-User-Rol"
     * @param userIdAutenticado ID del usuario autenticado - header "X-User-Id"
     * @return Lista de membresías del socio con código HTTP 200, o excepción si no
     *         tiene permisos o no encuentra datos
     */
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<List<SocioMembresiaResponseDTO>> consultarMembresiasSocio(
            @PathVariable Long idSocio,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {
        try {
            List<SocioMembresiaResponseDTO> membresias = socioMembresiaService.consultarMembresiasSocio(
                    idSocio, userRol, userIdAutenticado);
            return ResponseEntity.ok(membresias);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar membresías", e);
        }
    }
}
