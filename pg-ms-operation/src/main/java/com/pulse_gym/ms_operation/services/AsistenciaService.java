package com.pulse_gym.ms_operation.services;

import com.pulse_gym.lb_common.dto.AsistenciaResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RegistroAsistenciaDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.operation.Asistencia;
import com.pulse_gym.lb_common.entity.operation.Sede;
import com.pulse_gym.lb_common.enums.EnumEstadoAcceso;
import com.pulse_gym.lb_common.enums.EnumTipoAcceso;
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

    private final AsistenciaRepository asistenciaRepository;
    private final SedeRepository sedeRepository;
    private final UsuarioClient usuarioClient;  // ← Feign Client (no RestTemplate)

    @Transactional
    public MessegeGlobalDTO registrarEntrada(RegistroAsistenciaDTO request) {

        Sede sede = sedeRepository.findById(request.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + request.getIdSede()));

        EnumTipoAcceso tipoAcceso;
        try {
            tipoAcceso = EnumTipoAcceso.valueOf(request.getTipoAcceso().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de acceso no válido. Debe ser WEB o APP");
        }

        // ¡Así de simple! Parece una llamada local
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

    public List<AsistenciaResponseDTO> consultarHistorialUsuario(Long idUsuario) {
        List<Asistencia> asistencias = asistenciaRepository.findByIdUsuarioOrderByFechaHoraEntradaDesc(idUsuario);

        if (asistencias.isEmpty()) {
            throw new RuntimeException("El usuario " + idUsuario + " no tiene registros de asistencia");
        }

        return asistencias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<AsistenciaResponseDTO> consultarAsistenciasPorSede(Long idSede) {
        
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

    public List<AsistenciaResponseDTO> consultarAsistenciasDelDia() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Asistencia> asistencias = asistenciaRepository.findByFechaHoraEntradaBetween(inicio, fin);

        return asistencias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    private MessegeGlobalDTO registrarAccesoDenegado(RegistroAsistenciaDTO request, Sede sede,
            EnumTipoAcceso tipoAcceso, String motivo) {
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