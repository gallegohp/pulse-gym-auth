package com.pulse_gym.ms_users.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.client.AuthServiceClient;
import com.pulse_gym.lb_common.dto.CertificacionRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.user.Certificacion;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.enums.EnumRol;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.repository.CertificacionRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificacionService {

    /** Cliente para interactuar con el servicio de autenticación */
    private final AuthServiceClient authServiceClient;

    /** Repositorio para gestionar las certificaciones */
    private final CertificacionRepository certificacionRepository;

    /** Repositorio para gestionar los perfiles de usuario */
    private final UsuarioPerfilRepository usuarioRepository;

    /**
     * Registra una nueva certificación para un entrenador específico.
     * @param requestDTO DTO con los datos de la certificación a registrar
     * @param userRol    Rol del usuario que realiza la acción (obtenido del token
     *                   de autenticación)
     * @return Mensaje de éxito o error en el registro de la certificación
     */
    @Transactional
    public MessegeGlobalDTO registrarCertificacion(CertificacionRequestDTO requestDTO, String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

        UsuarioPerfil entrenador = usuarioRepository.findById(requestDTO.getIdEntrenador())
                .orElseThrow(() -> new RuntimeException("Entrenador no encontrado con ID: " + requestDTO.getIdEntrenador()));

        EnumRol rolEntrenador = authServiceClient.obtenerRolPorEmail(entrenador.getEmail());

        if (rolEntrenador == null) {
            throw new RuntimeException("No se pudo verificar el rol del usuario");
        }

        if (rolEntrenador != EnumRol.entrenador) {
            throw new RuntimeException("Solo se pueden registrar certificaciones para usuarios con rol ENTRENADOR. Rol actual: " + rolEntrenador);
        }

        Certificacion certificacion = new Certificacion();
        certificacion.setEntrenador(entrenador);
        certificacion.setNombre(requestDTO.getNombre());
        certificacion.setUrlPdf(requestDTO.getUrlPdf());

        certificacionRepository.save(certificacion);
        return new MessegeGlobalDTO("Certificación registrada correctamente");
    }
}

