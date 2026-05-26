package com.pulse_gym.ms_users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilRequestDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioPerfilService {

    /**
     * Repositorio para operaciones de base de datos de usuarios
     */
    private final UsuarioPerfilRepository usuarioRepository;

    /**
     * Crea un nuevo usuario en el sistema
     * 
     * @param requestDTO Datos completos del usuario a crear
     * @param userRol    Rol del usuario autenticado (debe ser administrador)
     * @return Mensaje de confirmación
     * @throws RuntimeException Si el documento de identidad ya existe
     */
    @Transactional
    public MessegeGlobalDTO crearUsuario(UsuarioPerfilRequestDTO requestDTO, String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

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
     * Obtiene la lista de todos los usuarios registrados
     * 
     * @param userRol Rol del usuario autenticado (debe ser administrador)
     * @return Lista de DTOs con los datos completos de cada usuario
     */
    @Transactional(readOnly = true)
    public List<UsuarioPerfilResponseDTO> obtenerTodosLosUsuarios(String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

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

    /**
     * NUEVO MÉTODO: Obtener usuario por ID (sin validación de rol)
     */
    @Transactional(readOnly = true)
    public UsuarioPerfilResponseDTO obtenerUsuarioPorId(Long idUsuario) {
        UsuarioPerfil usuario = usuarioRepository.findById(idUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
        
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
    }
}