package com.pulse_gym.ms_users.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pulse_gym.lb_common.dto.MembresiaRequestDTO;
import com.pulse_gym.lb_common.dto.MembresiaResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.ms_users.service.MembresiaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/membresias")
@RequiredArgsConstructor
public class MembresiaController {

    /** El servicio de membresías */
    private final MembresiaService membresiaService;

    /**
     * Endpoint para crear una nueva membresía
     * 
     * @param requestDTO Los datos para crear la membresía
     * @param userRol    El rol del usuario que realiza la acción (obtenido del
     *                   header "X-User-Rol")
     * @return Un mensaje global con la información de la membresía creada
     */
    @PostMapping
    public ResponseEntity<MessegeGlobalDTO> crearMembresia(
            @Valid @RequestBody MembresiaRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            MessegeGlobalDTO response = membresiaService.crearMembresia(requestDTO, userRol);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al crear membresía", e);
        }
    }

    /**
     * Endpoint para consultar las membresías
     * 
     * @param incluyeIA  Indica si se deben mostrar solo las membresías que incluyen IA
     * @param esFlexible Indica si se deben mostrar solo las membresías flexibles
     * @param userRol    El rol del usuario que realiza la acción (obtenido del
     *                   header "X-User-Rol")
     * @return Una lista con las membresías que cumplen con los criterios de búsqueda
     */
    @GetMapping
    public ResponseEntity<List<MembresiaResponseDTO>> consultarMembresias(
            @RequestParam(required = false) Boolean incluyeIA,
            @RequestParam(required = false) Boolean esFlexible,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            List<MembresiaResponseDTO> membresias = membresiaService.consultarMembresias(userRol, incluyeIA,
                    esFlexible);
            return ResponseEntity.status(HttpStatus.OK).body(membresias);
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
