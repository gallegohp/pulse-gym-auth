package com.pulse_gym.ms_users.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.client.AuthServiceClient;
import com.pulse_gym.lb_common.dto.HistorialFisicoRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.user.HistorialFisico;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.enums.EnumRol;
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
}
