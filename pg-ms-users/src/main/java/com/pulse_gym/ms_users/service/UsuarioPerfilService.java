package com.pulse_gym.ms_users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilRequestDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioPerfilService {

    /**
     * Inyeccion de UsuarioPerfilRepository para manejar las operaciones de base de datos relacionadas con los perfiles de usuario
     */
    private final UsuarioPerfilRepository usuarioRepository;

    /**
     * Valida que el rol del usuario actual sea "administrador". Si el rol es nulo o no es "administrador", lanza una excepción de autorización de seguridad.
     * @param currentRole el rol del usuario actual extraído del encabezado de la solicitud
     * @throws SecurityAuthorizationException si el rol es nulo o no es "administrador"
     */
    private void validateAdminRole(String currentRole) {
        System.out.println("DEBUG - Validando rol. Rol actual: '" + currentRole + "'");
        if (currentRole == null || !"administrador".equals(currentRole)) {
            throw new SecurityAuthorizationException(
                "Acceso denegado. Se requiere rol de administrador. Rol actual: " + currentRole);
        }
    }

    /**
     * Crea un nuevo usuario en el sistema. Primero valida que el usuario tenga el rol de administrador, luego verifica que el número de documento de 
     * identidad no exista ya en la base de datos.
     * @param requestDTO
     * @param userRol
     * @return MessegeGlobalDTO con un mensaje de éxito si el usuario se creó correctamente
     */
    @Transactional
    public MessegeGlobalDTO crearUsuario(UsuarioPerfilRequestDTO requestDTO, String userRol) {
        validateAdminRole(userRol);

        if (usuarioRepository.findByDocumentoIdentidad(requestDTO.getDocumentoIdentidad()).isPresent()) {
            throw new RuntimeException("El número de documento ya existe, por favor ingrese uno diferente: "
                    + requestDTO.getDocumentoIdentidad());
        }

        UsuarioPerfil usuario = new UsuarioPerfil();
        usuario.setRol(requestDTO.getRol());
        usuario.setNombre(requestDTO.getNombre());
        usuario.setApellido(requestDTO.getApellido());
        usuario.setTelefono(requestDTO.getTelefono());
        usuario.setDocumentoIdentidad(requestDTO.getDocumentoIdentidad());
        usuario.setFotoUrl(requestDTO.getFotoUrl());
        usuario.setFechaContratacion(requestDTO.getFechaContratacion());
        usuario.setEspecialidad(requestDTO.getEspecialidad());
        usuario.setAnosExperiencia(requestDTO.getAnosExperiencia());
        usuario.setHorarioDisponibilidad(requestDTO.getHorarioDisponibilidad());
        usuario.setTarifaHora(requestDTO.getTarifaHora());
        usuario.setTurno(requestDTO.getTurno());
        usuario.setFechaNacimiento(requestDTO.getFechaNacimiento());
        usuario.setContactoEmergenciaNombre(requestDTO.getContactoEmergenciaNombre());
        usuario.setContactoEmergenciaTelefono(requestDTO.getContactoEmergenciaTelefono());
        usuario.setObjetivoPrincipal(requestDTO.getObjetivoPrincipal());
        usuario.setNivelExperiencia(requestDTO.getNivelExperiencia());
        usuario.setIdSede(requestDTO.getIdSede());

        usuarioRepository.save(usuario);
        return new MessegeGlobalDTO("Usuario creado ¡Correctamente!");
    }

    /**
     * Obtiene la lista de todos los usuarios registrados en el sistema. Primero valida que el usuario tenga el rol de administrador
     * @param userRol
     * @return ResponseEntity<List<UsuarioPerfilResponseDTO>> 
     */
    @Transactional(readOnly = true)
    public List<UsuarioPerfilResponseDTO> obtenerTodosLosUsuarios(String userRol) {
        validateAdminRole(userRol);

        return usuarioRepository.findAll().stream().map(usuario -> {
            UsuarioPerfilResponseDTO dto = new UsuarioPerfilResponseDTO();
            dto.setIdUsuario(usuario.getIdUsuario());
            dto.setRol(usuario.getRol());
            dto.setNombre(usuario.getNombre());
            dto.setApellido(usuario.getApellido());
            dto.setTelefono(usuario.getTelefono());
            dto.setDocumentoIdentidad(usuario.getDocumentoIdentidad());
            dto.setFotoUrl(usuario.getFotoUrl());
            dto.setFechaContratacion(usuario.getFechaContratacion());
            dto.setEspecialidad(usuario.getEspecialidad());
            dto.setAnosExperiencia(usuario.getAnosExperiencia());
            dto.setHorarioDisponibilidad(usuario.getHorarioDisponibilidad());
            dto.setTarifaHora(usuario.getTarifaHora());
            dto.setTurno(usuario.getTurno());
            dto.setFechaNacimiento(usuario.getFechaNacimiento());
            dto.setContactoEmergenciaNombre(usuario.getContactoEmergenciaNombre());
            dto.setContactoEmergenciaTelefono(usuario.getContactoEmergenciaTelefono());
            dto.setObjetivoPrincipal(usuario.getObjetivoPrincipal());
            dto.setNivelExperiencia(usuario.getNivelExperiencia());
            dto.setFechaRegistro(usuario.getFechaRegistro());
            dto.setIdSede(usuario.getIdSede());
            return dto;
        }).collect(Collectors.toList());
    }
}