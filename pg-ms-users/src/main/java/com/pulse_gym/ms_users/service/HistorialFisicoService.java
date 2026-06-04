package com.pulse_gym.ms_users.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.client.AuthServiceClient;
import com.pulse_gym.lb_common.dto.HistorialFisicoRequestDTO;
import com.pulse_gym.lb_common.dto.HistorialFisicoResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.user.HistorialFisico;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.enums.EnumRol;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.repository.HistorialFisicoRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistorialFisicoService {

    /** Repositorio del historial físico */
    private final HistorialFisicoRepository historialRepository;

    /** Repositorio del perfil del usuario */
    private final UsuarioPerfilRepository usuarioRepository;

    /** Cliente del servicio de autenticación */
    private final AuthServiceClient authServiceClient;

    /**
     * Registra una nueva medición física para un socio
     * 
     * @param historial Medición física a registrar
     * @return Mensaje de éxito si la medición se registró correctamente
     */
    private HistorialFisicoResponseDTO convertirAResponseDTO(HistorialFisico historial) {
        HistorialFisicoResponseDTO dto = new HistorialFisicoResponseDTO();
        dto.setIdHistorialFisico(historial.getIdHistorialFisico());
        dto.setIdSocio(historial.getSocio().getIdUsuario());
        dto.setNombreSocio(historial.getSocio().getNombre() + " " + historial.getSocio().getApellido());

        if (historial.getRecepcionista() != null) {
            dto.setIdRecepcionista(historial.getRecepcionista().getIdUsuario());
            dto.setNombreRecepcionista(
                    historial.getRecepcionista().getNombre() + " " + historial.getRecepcionista().getApellido());
        }

        dto.setFechaMedicion(historial.getFechaMedicion());
        dto.setPesoKg(historial.getPesoKg());
        dto.setPorcentajeGrasa(historial.getPorcentajeGrasa());
        dto.setPorcentajeMusculo(historial.getPorcentajeMusculo());
        dto.setCinturaCm(historial.getCinturaCm());
        dto.setPechoCm(historial.getPechoCm());
        dto.setBrazoIzqCm(historial.getBrazoIzqCm());
        dto.setBrazoDerCm(historial.getBrazoDerCm());
        dto.setPiernaIzqCm(historial.getPiernaIzqCm());
        dto.setPiernaDerCm(historial.getPiernaDerCm());

        return dto;
    }

    /**
     * Registra una nueva medición física para un socio
     * 
     * @param requestDTO        Datos de la medición física a registrar
     * @param userRol           Rol del usuario autenticado
     * @param userIdAutenticado ID del usuario autenticado
     * @return Mensaje de éxito si la medición se registró correctamente
     */
    @Transactional
    public MessegeGlobalDTO registrarMedicion(HistorialFisicoRequestDTO requestDTO, String userRol,
            Long userIdAutenticado) {
        ValidacionDeRoles.validarAdminOEntrenadorORecepcionista(userRol);

        UsuarioPerfil socio = usuarioRepository.findById(requestDTO.getIdSocio())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + requestDTO.getIdSocio()));

        EnumRol rolSocio = authServiceClient.obtenerRolPorEmail(socio.getEmail());
        if (rolSocio == null || rolSocio != EnumRol.socio) {
            throw new RuntimeException("El usuario no es un socio. Rol actual: " + rolSocio);
        }

        UsuarioPerfil recepcionista = null;
        if (requestDTO.getIdRecepcionista() != null) {
            recepcionista = usuarioRepository.findById(requestDTO.getIdRecepcionista())
                    .orElseThrow(() -> new RuntimeException(
                            "Recepcionista no encontrado con ID: " + requestDTO.getIdRecepcionista()));
        }

        HistorialFisico historial = new HistorialFisico();
        historial.setSocio(socio);
        historial.setRecepcionista(recepcionista);
        historial.setFechaMedicion(
                requestDTO.getFechaMedicion() != null ? requestDTO.getFechaMedicion() : LocalDateTime.now());
        historial.setPesoKg(requestDTO.getPesoKg());
        historial.setPorcentajeGrasa(requestDTO.getPorcentajeGrasa());
        historial.setPorcentajeMusculo(requestDTO.getPorcentajeMusculo());
        historial.setCinturaCm(requestDTO.getCinturaCm());
        historial.setPechoCm(requestDTO.getPechoCm());
        historial.setBrazoIzqCm(requestDTO.getBrazoIzqCm());
        historial.setBrazoDerCm(requestDTO.getBrazoDerCm());
        historial.setPiernaIzqCm(requestDTO.getPiernaIzqCm());
        historial.setPiernaDerCm(requestDTO.getPiernaDerCm());

        historialRepository.save(historial);

        return new MessegeGlobalDTO("Medición física registrada correctamente para el socio: " + socio.getNombre());
    }

    /**
     * Consulta el historial físico de un socio
     * 
     * @param idSocio           ID del socio
     * @param userRol           Rol del usuario autenticado
     * @param userIdAutenticado ID del usuario autenticado
     * @return Lista de mediciones físicas del socio
     */
    @Transactional(readOnly = true)
    public List<HistorialFisicoResponseDTO> consultarHistorial(Long idSocio, String userRol, Long userIdAutenticado) {

        if (userRol.equals(EnumRol.socio.name())) {
            if (!userIdAutenticado.equals(idSocio)) {
                throw new SecurityAuthorizationException("Acceso denegado. Solo puede ver su propio historial físico");
            }
        } else if (!userRol.equals(EnumRol.administrador.name()) &&
                !userRol.equals(EnumRol.entrenador.name()) &&
                !userRol.equals(EnumRol.recepcionista.name())) {
            throw new SecurityAuthorizationException("Acceso denegado. Rol no autorizado: " + userRol);
        }

        UsuarioPerfil socio = usuarioRepository.findById(idSocio)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + idSocio));

        List<HistorialFisico> historial = historialRepository.findBySocio_IdUsuarioOrderByFechaMedicionDesc(idSocio);

        if (historial.isEmpty()) {
            throw new RuntimeException("El socio " + socio.getNombre() + " no tiene registros de historial físico");
        }

        return historial.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

}
