package com.pulse_gym.ms_users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.ms_users.dto.MessageResponseDTO;
import com.pulse_gym.ms_users.dto.UsuarioPerfilRequestDTO;
import com.pulse_gym.ms_users.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.ms_users.entity.UsuarioPerfil;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioPerfilService {

    private final UsuarioPerfilRepository usuarioRepository;

    @Transactional
    public MessageResponseDTO crearUsuario(UsuarioPerfilRequestDTO requestDTO) {

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

        return new MessageResponseDTO("Usuario creado ¡Correctamente!");
    }

    @Transactional(readOnly = true)
    public List<UsuarioPerfilResponseDTO> obtenerTodosLosUsuarios() {
        // Obtenemos la lista de la BD y la mapeamos directamente en el stream al ResponseDTO
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