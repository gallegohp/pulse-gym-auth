package com.pulse_gym.ms_users.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilRequestDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.enums.EnumEstadoUsuario;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
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
     * Convierte una entidad UsuarioPerfil a su correspondiente DTO de respuesta
     * 
     * @param usuario Entidad de usuario a convertir (no puede ser nulo)
     * @return DTO con todos los datos del usuario mapeados desde la entidad
     */
    private UsuarioPerfilResponseDTO convertirADTO(UsuarioPerfil usuario) {
        UsuarioPerfilResponseDTO dto = new UsuarioPerfilResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
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
        dto.setEstado(usuario.getEstado());
        return dto;
    }

    /**
     * Crea un nuevo usuario en el sistema
     * 
     * @param requestDTO Datos del usuario a crear
     * @param userRol    Rol del usuario autenticado
     * @return Mensaje de confirmación
     */
    @Transactional
    public MessegeGlobalDTO crearUsuario(UsuarioPerfilRequestDTO requestDTO, String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

        if (usuarioRepository.findByDocumentoIdentidad(requestDTO.getDocumentoIdentidad()).isPresent()) {
            throw new RuntimeException("El número de documento ya existe, por favor ingrese uno diferente: "
                    + requestDTO.getDocumentoIdentidad());
        }

        UsuarioPerfil usuario = new UsuarioPerfil();
        usuario.setNombre(requestDTO.getNombre());
        usuario.setApellido(requestDTO.getApellido());
        usuario.setEmail(requestDTO.getEmail());
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
        usuario.setEstado(EnumEstadoUsuario.ACTIVO);

        usuarioRepository.save(usuario);
        return new MessegeGlobalDTO("Usuario creado ¡Correctamente!");
    }

    /**
     * Obtiene la lista de todos los usuarios activos
     * 
     * @param userRol Rol del usuario autenticado
     * @return Lista de DTOs con los datos de los usuarios activos
     */
    @Transactional(readOnly = true)
    public List<UsuarioPerfilResponseDTO> obtenerTodosLosUsuariosActivo(String userRol) {
        ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

        return usuarioRepository.findByEstado(EnumEstadoUsuario.ACTIVO).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioPerfilResponseDTO> obtenerTodosLosUsuariosInactivo(String userRol) {
        ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

        return usuarioRepository.findByEstado(EnumEstadoUsuario.INACTIVO).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioPerfilResponseDTO> obtenerTodosLosUsuarios(String userRol) {
        ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario activo por su ID
     * 
     * @param idUsuario ID del usuario a buscar
     * @param userRol   Rol del usuario autenticado
     * @return DTO con los datos del usuario
     */
    @Transactional(readOnly = true)
    public UsuarioPerfilResponseDTO obtenerUsuarioPorId(Long idUsuario, String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

        if (idUsuario == null) {
            throw new RuntimeException("El ID del usuario no puede ser nulo");
        }

        UsuarioPerfil usuario = usuarioRepository.findByIdAndEstado(idUsuario, EnumEstadoUsuario.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        return convertirADTO(usuario);
    }

    /**
     * Obtiene un usuario activo por su número de documento
     * 
     * @param documentoIdentidad Número de documento del usuario
     * @param userRol            Rol del usuario autenticado
     * @return DTO con los datos del usuario
     */
    @Transactional(readOnly = true)
    public UsuarioPerfilResponseDTO obtenerUsuarioPorNumeroDocumento(String documentoIdentidad, String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

        if (documentoIdentidad == null || documentoIdentidad.trim().isEmpty()) {
            throw new RuntimeException("El número de documento no puede ser nulo o vacío");
        }

        UsuarioPerfil usuario = usuarioRepository
                .findByDocumentoIdentidadAndEstado(documentoIdentidad, EnumEstadoUsuario.ACTIVO)
                .orElseThrow(() -> new RuntimeException(
                        "Usuario no encontrado con número de documento: " + documentoIdentidad));

        return convertirADTO(usuario);
    }

    /**
     * Obtiene un usuario activo por su nombre
     * 
     * @param nombre  Nombre del usuario a buscar
     * @param userRol Rol del usuario autenticado
     * @return DTO con los datos del usuario
     */
    @Transactional(readOnly = true)
    public List<UsuarioPerfilResponseDTO> obtenerUsuariosPorNombre(String nombre, String userRol) {
        ValidacionDeRoles.validarCualquierRol(userRol);

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre del usuario no puede ser nulo o vacío");
        }

        String nombreLimpio = nombre.trim();

        List<UsuarioPerfil> usuarios = usuarioRepository
                .findByNombreIgnoreCaseAndEstado(nombreLimpio, EnumEstadoUsuario.ACTIVO);

        if (usuarios.isEmpty()) {
            throw new RuntimeException("No se encontraron usuarios con nombre: " + nombre);
        }

        return usuarios.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de un usuario según el rol
     * - Admin/Recepcionista: Pueden actualizar todos los campos
     * - Socio: Solo puede actualizar datos de contacto (teléfono, email, dirección)
     * 
     * @param idUsuario  ID del usuario a actualizar
     * @param requestDTO Datos actualizados
     * @param userRol    Rol del usuario que hace la petición
     * @param userEmail  Email del usuario autenticado (para validar que socio solo
     *                   actualice su propio perfil)
     * @return Mensaje de confirmación
     */
    @Transactional
    public MessegeGlobalDTO actualizarUsuario(Long idUsuario, UsuarioPerfilRequestDTO requestDTO,
            String userRol, String userEmail) {
        if (idUsuario == null) {
            throw new RuntimeException("El ID del usuario no puede ser nulo");
        }

        UsuarioPerfil usuarioExistente = usuarioRepository.findByIdAndEstado(idUsuario, EnumEstadoUsuario.ACTIVO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo con ID: " + idUsuario));

        if (usuarioExistente == null) {
            throw new RuntimeException("Error: Usuario no encontrado");

        }

        if (userRol.equals("socio")) {

            if (!usuarioExistente.getEmail().equals(userEmail)) {
                throw new SecurityAuthorizationException(
                        "Acceso denegado. Solo puede actualizar su propio perfil");
            }

            actualizarDatosContacto(usuarioExistente, requestDTO);

        } else if (userRol.equals("administrador") || userRol.equals("recepcionista")) {

            actualizarTodosLosCampos(usuarioExistente, requestDTO);

        } else {
            throw new SecurityAuthorizationException(
                    "Acceso denegado. Rol no autorizado para actualizar usuarios: " + userRol);
        }

        usuarioRepository.save(usuarioExistente);
        return new MessegeGlobalDTO("Usuario actualizado correctamente");
    }

    /**
     * Actualiza solo los datos de contacto del usuario (teléfono y email)
     * 
     * @param usuario    Entidad del usuario a actualizar
     * @param requestDTO DTO con los nuevos datos de contacto
     */
    private void actualizarDatosContacto(UsuarioPerfil usuario, UsuarioPerfilRequestDTO requestDTO) {

        if (requestDTO.getNombre() != null && !requestDTO.getNombre().isEmpty()) {
            usuario.setNombre(requestDTO.getNombre());
        }

        if (requestDTO.getApellido() != null && !requestDTO.getApellido().isEmpty()) {
            usuario.setApellido(requestDTO.getApellido());
        }

        if (requestDTO.getEmail() != null && !requestDTO.getEmail().isEmpty()) {
            usuario.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getTelefono() != null) {
            usuario.setTelefono(requestDTO.getTelefono());
        }

        if (requestDTO.getEmail() != null && !requestDTO.getEmail().isEmpty()) {
            usuario.setEmail(requestDTO.getEmail());
        }

        if (requestDTO.getDocumentoIdentidad() != null && !requestDTO.getDocumentoIdentidad().isEmpty()) {
            usuario.setDocumentoIdentidad(requestDTO.getDocumentoIdentidad());

        }
    }

    /**
     * Actualiza todos los campos del usuario (para administradores y
     * recepcionistas)
     * 
     * @param usuario    Entidad del usuario a actualizar
     * @param requestDTO DTO con los nuevos datos del usuario
     */
    private void actualizarTodosLosCampos(UsuarioPerfil usuario, UsuarioPerfilRequestDTO requestDTO) {

        if (requestDTO.getNombre() != null) {
            usuario.setNombre(requestDTO.getNombre());
        }

        if (requestDTO.getApellido() != null) {
            usuario.setApellido(requestDTO.getApellido());
        }

        if (requestDTO.getTelefono() != null) {
            usuario.setTelefono(requestDTO.getTelefono());
        }

        if (requestDTO.getEmail() != null && !requestDTO.getEmail().isEmpty()) {

            usuario.setEmail(requestDTO.getEmail());
        }

        if (requestDTO.getDocumentoIdentidad() != null && !requestDTO.getDocumentoIdentidad().isEmpty()) {
            usuario.setDocumentoIdentidad(requestDTO.getDocumentoIdentidad());

        }
        if (requestDTO.getFotoUrl() != null) {
            usuario.setFotoUrl(requestDTO.getFotoUrl());
        }

        if (requestDTO.getFechaContratacion() != null) {
            usuario.setFechaContratacion(requestDTO.getFechaContratacion());
        }

        if (requestDTO.getEspecialidad() != null) {
            usuario.setEspecialidad(requestDTO.getEspecialidad());
        }

        if (requestDTO.getAnosExperiencia() != null) {
            usuario.setAnosExperiencia(requestDTO.getAnosExperiencia());
        }

        if (requestDTO.getHorarioDisponibilidad() != null) {
            usuario.setHorarioDisponibilidad(requestDTO.getHorarioDisponibilidad());
        }

        if (requestDTO.getTarifaHora() != null) {
            usuario.setTarifaHora(requestDTO.getTarifaHora());
        }

        if (requestDTO.getTurno() != null) {
            usuario.setTurno(requestDTO.getTurno());
        }

        if (requestDTO.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(requestDTO.getFechaNacimiento());
        }

        if (requestDTO.getContactoEmergenciaNombre() != null) {
            usuario.setContactoEmergenciaNombre(requestDTO.getContactoEmergenciaNombre());
        }

        if (requestDTO.getContactoEmergenciaTelefono() != null) {
            usuario.setContactoEmergenciaTelefono(requestDTO.getContactoEmergenciaTelefono());
        }

        if (requestDTO.getObjetivoPrincipal() != null) {
            usuario.setObjetivoPrincipal(requestDTO.getObjetivoPrincipal());
        }

        if (requestDTO.getNivelExperiencia() != null) {
            usuario.setNivelExperiencia(requestDTO.getNivelExperiencia());
        }

        if (requestDTO.getIdSede() != null) {
            usuario.setIdSede(requestDTO.getIdSede());
        }
    }

    /**
     * Cambia el estado de un usuario (ACTIVO/INACTIVO)
     * 
     * @param idUsuario ID del usuario
     * @param estado    Nuevo estado del usuario
     * @param userRol   Rol del usuario autenticado
     * @return Mensaje de confirmación
     */
    @Transactional
    public MessegeGlobalDTO cambiarEstadoUsuario(Long idUsuario, EnumEstadoUsuario estado, String userRol) {

        ValidacionDeRoles.validarAdminORecepcionista(userRol);

        if (idUsuario == null) {
            throw new RuntimeException("El ID del usuario no puede ser nulo");
        }

        UsuarioPerfil usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));

        if (usuario.getEstado() == estado) {
            String mensajeActual = estado == EnumEstadoUsuario.ACTIVO
                    ? "El usuario ya está activo"
                    : "El usuario ya está inactivo";
            throw new RuntimeException(mensajeActual);
        }

        usuario.setEstado(estado);
        usuarioRepository.save(usuario);

        String mensaje = estado == EnumEstadoUsuario.ACTIVO
                ? "Usuario activado correctamente"
                : "Usuario desactivado correctamente";

        return new MessegeGlobalDTO(mensaje);
    }

    /**
     * Desactiva un usuario (cambia a estado INACTIVO)
     * 
     * @param idUsuario ID del usuario
     * @param userRol   Rol del usuario autenticado
     * @return Mensaje de confirmación
     */
    @Transactional
    public MessegeGlobalDTO desactivarUsuario(Long idUsuario, String userRol) {
        return cambiarEstadoUsuario(idUsuario, EnumEstadoUsuario.INACTIVO, userRol);
    }

    /**
     * Activa un usuario (cambia a estado ACTIVO)
     * 
     * @param idUsuario ID del usuario
     * @param userRol   Rol del usuario autenticado
     * @return Mensaje de confirmación
     */
    @Transactional
    public MessegeGlobalDTO activarUsuario(Long idUsuario, String userRol) {
        return cambiarEstadoUsuario(idUsuario, EnumEstadoUsuario.ACTIVO, userRol);
    }

}