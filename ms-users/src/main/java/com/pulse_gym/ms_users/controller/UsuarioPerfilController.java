package com.pulse_gym.ms_users.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.ms_users.dto.MessageResponseDTO;
import com.pulse_gym.ms_users.dto.UsuarioPerfilRequestDTO;
import com.pulse_gym.ms_users.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.ms_users.service.UsuarioPerfilService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioPerfilController {

    /** Servicio del perfil de ususario */
    private final UsuarioPerfilService usuarioService;

    /**
     * Endpoint encargado de crear un nuevo perfil de usuario.
     * Recibe la información del cuerpo de la petición, la valida y delega el
     * proceso al servicio de negocio.
     *
     * @param requestDTO Objeto con la información de registro del usuario
     *                   debidamente validada.
     * @return {@link ResponseEntity} que contiene un {@link MessageResponseDTO} con
     *         el resultado del proceso
     *         y el código de estado HTTP correspondiente (201 Created, 400 Bad
     *         Request o 500 Internal Server Error).
     */
    @PostMapping
    public ResponseEntity<MessageResponseDTO> crearUsuario(@Valid @RequestBody UsuarioPerfilRequestDTO requestDTO) {
        try {
            MessageResponseDTO response = usuarioService.crearUsuario(requestDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new MessageResponseDTO("Error al crear el usuario: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponseDTO("Error interno del servidor: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint encargado de recuperar el listado completo de los perfiles de
     * usuario en la plataforma.
     *
     * @return {@link ResponseEntity} con la lista de
     *         {@link UsuarioPerfilResponseDTO} si existen registros (200 OK),
     *         una respuesta vacía si no se encuentra ningún usuario (204 No
     *         Content), o un mensaje de error
     *         en caso de una falla en el servidor (500 Internal Server Error).
     */
    @GetMapping
    public ResponseEntity<?> obtenerTodosLosUsuarios() {
        try {
            List<UsuarioPerfilResponseDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();

            if (usuarios.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new MessageResponseDTO("Error al obtener la lista de usuarios: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
