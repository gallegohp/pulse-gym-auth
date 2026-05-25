package com.pulse_gym.ms_operation.services;

import com.pulse_gym.lb_common.dto.AsistenciaResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RegistroAsistenciaDTO;
import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;
import com.pulse_gym.lb_common.entity.operation.Asistencia;
import com.pulse_gym.lb_common.entity.operation.Sede;
import com.pulse_gym.lb_common.enums.EnumEstadoAcceso;
import com.pulse_gym.lb_common.enums.EnumTipoAcceso;
import com.pulse_gym.ms_operation.repository.AsistenciaRepository;
import com.pulse_gym.ms_operation.repository.SedeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsistenciaService {

    private AsistenciaRepository asistenciaRepository;

    private SedeRepository sedeRepository;

    private RestTemplate restTemplate;

    @Value("${microservicio.usuarios.url:http://localhost:8081}")
    private String usuariosServiceUrl;

    // 1. Registrar asistencia
    @Transactional
    public MessegeGlobalDTO registrarEntrada(RegistroAsistenciaDTO request) {

        // Validar que la sede existe
        Sede sede = sedeRepository.findById(request.getIdSede())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + request.getIdSede()));

        // Validar tipo de acceso
        EnumTipoAcceso tipoAcceso;
        try {
            tipoAcceso = EnumTipoAcceso.valueOf(request.getTipoAcceso().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de acceso no válido. Debe ser WEB o APP");
        }

        // Validar que el usuario existe
        UsuarioPerfilResponseDTO usuario = validarUsuario(request.getIdUsuario());

        if (usuario == null) {
            return registrarAccesoDenegado(request, sede, tipoAcceso,
                    "Usuario no encontrado con ID: " + request.getIdUsuario());
        }

        // Registrar acceso permitido
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
                "✅ Acceso permitido. Bienvenido %s, registro exitoso en sede: %s",
                nombreCompleto,
                sede.getNombreSede()));
    }

    // 2. Consultar historial de un usuario
    public List<AsistenciaResponseDTO> consultarHistorialUsuario(Long idUsuario) {
        List<Asistencia> asistencias = asistenciaRepository.findByIdUsuarioOrderByFechaHoraEntradaDesc(idUsuario);

        if (asistencias.isEmpty()) {
            throw new RuntimeException("El usuario " + idUsuario + " no tiene registros de asistencia");
        }

        return asistencias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // 3. Consultar asistencias por sede
    public List<AsistenciaResponseDTO> consultarAsistenciasPorSede(Long idSede) {
        // Validar que la sede existe
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

    // 4. Consultar asistencias del día actual
    public List<AsistenciaResponseDTO> consultarAsistenciasDelDia() {
        LocalDateTime inicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime fin = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        List<Asistencia> asistencias = asistenciaRepository.findByFechaHoraEntradaBetween(inicio, fin);

        return asistencias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // Validar usuario contra el microservicio
    private UsuarioPerfilResponseDTO validarUsuario(Long idUsuario) {
        try {
            String url = usuariosServiceUrl + "/api/usuarios/" + idUsuario;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
            ResponseEntity<UsuarioPerfilResponseDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    UsuarioPerfilResponseDTO.class);

            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error al validar usuario ID " + idUsuario + ": " + e.getMessage());

            // Modo prueba
            String modoPrueba = System.getProperty("modo.prueba", "false");
            if ("true".equals(modoPrueba)) {
                System.err.println("⚠️ MODO PRUEBA: Simulando usuario válido");
                UsuarioPerfilResponseDTO mock = new UsuarioPerfilResponseDTO();
                mock.setIdUsuario(idUsuario);
                mock.setNombre("Usuario");
                mock.setApellido("Prueba");
                return mock;
            }

            return null;
        }
    }

    // Registrar acceso denegado
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

        throw new RuntimeException("❌ Acceso denegado: " + motivo);
    }

    // Convertir a DTO
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