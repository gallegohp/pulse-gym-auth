package com.pulse_gym.ms_operation.services;

import com.pulse_gym.lb_common.dto.AsistenciaResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RegistroAsistenciaDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.operation.Asistencia;
import com.pulse_gym.lb_common.entity.operation.Sede;
import com.pulse_gym.lb_common.enums.EnumEstadoAcceso;
import com.pulse_gym.lb_common.enums.EnumTipoAcceso;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_operation.client.UsuarioClient;
import com.pulse_gym.ms_operation.repository.AsistenciaRepository;
import com.pulse_gym.ms_operation.repository.SedeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    /**
     * Inyeccion de AsistenciaRepository para manejar la lógica de negocio relacionada con
     * las asistencias, como el registro y la obtención de asistencias.
     */
    private final AsistenciaRepository asistenciaRepository;

    /**
     * Inyeccion de SedeRepository para manejar la lógica de negocio relacionada con
     * las sedes, como el registro y la obtención de sedes.
     */ 
    private final SedeRepository sedeRepository;

    /**
     * Inyeccion de UsuarioClient para manejar la lógica de negocio relacionada con
     * los usuarios, como el registro y la obtención de usuarios.
     */
    private final UsuarioClient usuarioClient;  

    /**
     * Registra una nueva asistencia en la base de datos.
     * 
     * Se valida que la peticion solo la puede hacer un socio
     * 
     * @param request
     * @param userRol Rol del usuario que hace la petición (desde header X-User-Rol)
     * @return MessegeGlobalDTO con un mensaje de éxito si la asistencia se registró correctamente
     */ 
    @Transactional
    public MessegeGlobalDTO registrarEntrada(RegistroAsistenciaDTO request, String userRol) {

        ValidacionDeRoles.validarSocio(userRol);

        Sede sede = sedeRepository.findById(request.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + request.getIdSede()));

        EnumTipoAcceso tipoAcceso;
        try {
            tipoAcceso = EnumTipoAcceso.valueOf(request.getTipoAcceso().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de acceso no válido. Debe ser WEB o APP");
        }

        UsuarioPerfilResponseDTO usuario = usuarioClient.obtenerUsuarioPorId(request.getIdUsuario());

        if (usuario == null) {
            return registrarAccesoDenegado(request, sede, tipoAcceso,
                    "Usuario no encontrado con ID: " + request.getIdUsuario());
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setIdUsuario(request.getIdUsuario());
        asistencia.setSede(sede);
        asistencia.setFechaHoraEntrada(LocalDateTime.now());
        asistencia.setTipoAcceso(tipoAcceso);
        asistencia.setEstadoAcceso(EnumEstadoAcceso.PERMITIDO);
        asistencia.setMotivoDenegacion(null);

        asistenciaRepository.save(asistencia);

        String nombreCompleto = (usuario.getNombre() != null ? usuario.getNombre() : "") +
                " " + (usuario.getApellido() != null ? usuario.getApellido() : "");
        nombreCompleto = nombreCompleto.trim().isEmpty() ? "Socio" : nombreCompleto;

        return new MessegeGlobalDTO(String.format(
                "Acceso permitido. Bienvenido %s, registro exitoso en sede: %s",
                nombreCompleto,
                sede.getNombreSede()));
    }

    /**
     * Obtiene los registros de asistencias de un usuario.
     * @param idUsuario
     * @param userRol Rol del usuario que hace la petición (desde header X-User-Rol)
     * @return List<AsistenciaResponseDTO> con los registros de asistencias encontrados     
     */
    public List<AsistenciaResponseDTO> consultarHistorialUsuario(Long idUsuario, String userRol) {
        
        ValidacionDeRoles.validarCualquierRol(userRol);

        List<Asistencia> asistencias = asistenciaRepository.findByIdUsuarioOrderByFechaHoraEntradaDesc(idUsuario);

        if (asistencias.isEmpty()) {
            throw new RuntimeException("El usuario " + idUsuario + " no tiene registros de asistencia");
        }

        return asistencias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los registros de asistencias de una sede.
     * 
     * Se valida que la peticion solo la puede hacer un entrenador, recepcionista o admin
     * 
     * @param idSede
     * @param userRol Rol del usuario
     * @return List<AsistenciaResponseDTO> con los registros de asistencias encontrados   
     */
    public List<AsistenciaResponseDTO> consultarAsistenciasPorSede(Long idSede, String userRol) {
        
        ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

        Sede sede = sedeRepository.findById(idSede)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + idSede));

        List<Asistencia> asistencias = asistenciaRepository.findBySedeIdSedeOrderByFechaHoraEntradaDesc(idSede);

        if (asistencias.isEmpty()) {
            throw new RuntimeException("No hay asistencias registradas para la sede: " + sede.getNombreSede());
        }

        return asistencias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los registros de asistencias del día actual.
     * 
     * Se valida que la peticion solo la puede hacer un entrenador, recepcionista o admin
     * @param userRol Rol del usuario
     * @return List<AsistenciaResponseDTO> con los registros de asistencias encontrados
     */
    public List<AsistenciaResponseDTO> consultarAsistenciasDelDia(String userRol) {

        ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Asistencia> asistencias = asistenciaRepository.findByFechaHoraEntradaBetween(inicio, fin);

        return asistencias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Registra un acceso denegado en la base de datos.
     * @param request
     * @param sede
     * @param tipoAcceso
     * @param motivo
     * @return MessegeGlobalDTO con un mensaje de éxito si el acceso se registró correctamente
     */
    private MessegeGlobalDTO registrarAccesoDenegado(RegistroAsistenciaDTO request, Sede sede, EnumTipoAcceso tipoAcceso, String motivo) {
        Asistencia asistencia = new Asistencia();
        asistencia.setIdUsuario(request.getIdUsuario());
        asistencia.setSede(sede);
        asistencia.setFechaHoraEntrada(LocalDateTime.now());
        asistencia.setTipoAcceso(tipoAcceso);
        asistencia.setEstadoAcceso(EnumEstadoAcceso.DENEGADO);
        asistencia.setMotivoDenegacion(motivo);

        asistenciaRepository.save(asistencia);

        throw new RuntimeException("Acceso denegado: " + motivo);
    }

    /**
     * Convierte un objeto Asistencia a un objeto AsistenciaResponseDTO.
     * @param asistencia
     * @return AsistenciaResponseDTO con los datos del asistencia
     */ 
    private AsistenciaResponseDTO convertirAResponseDTO(Asistencia asistencia) {
        AsistenciaResponseDTO dto = new AsistenciaResponseDTO();
        dto.setIdAsistencia(asistencia.getIdAsistencia());
        dto.setIdUsuario(asistencia.getIdUsuario());
        dto.setNombreSede(asistencia.getSede() != null ? asistencia.getSede().getNombreSede() : "Sede no registrada");
        dto.setFechaHoraEntrada(asistencia.getFechaHoraEntrada());
        dto.setTipoAcceso(asistencia.getTipoAcceso().name());
        dto.setEstadoAcceso(asistencia.getEstadoAcceso().name());
        dto.setMotivoDenegacion(asistencia.getMotivoDenegacion());
        return dto;
    }
}