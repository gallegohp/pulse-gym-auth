package com.pulse_gym.ms_users.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilRequestDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.ms_users.service.UsuarioPerfilService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioPerfilController {

    /**
     * Servicio de usuarios para la lógica de negocio
     */
    private final UsuarioPerfilService usuarioService;

    /**
     * Crea un nuevo usuario en el sistema
     * 
     * @param requestDTO Datos del usuario a crear
     * @param userRol    Rol del usuario que hace la petición (desde header
     *                   X-User-Rol)
     * @return Respuesta con mensaje de éxito o error
     */
    @PostMapping
    public ResponseEntity<MessegeGlobalDTO> crearUsuario(
            @Valid @RequestBody UsuarioPerfilRequestDTO requestDTO,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {
        try {
            MessegeGlobalDTO response = usuarioService.crearUsuario(requestDTO, userRol);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(new MessegeGlobalDTO("Error interno del servidor: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene la lista de todos los usuarios registrados
     * 
     * @param userRol Rol del usuario que hace la petición (desde header X-User-Rol)
     * @return Lista de usuarios con sus datos completos
     */
    @GetMapping
    public ResponseEntity<List<UsuarioPerfilResponseDTO>> obtenerTodosLosUsuarios(
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        try {
            List<UsuarioPerfilResponseDTO> usuarios = usuarioService.obtenerTodosLosUsuarios(userRol);
            return ResponseEntity.status(HttpStatus.OK).body(usuarios);
        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener la lista de usuarios",
                    e);
        }
    }
}