package com.pulse_gym.ms_users.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.pulse_gym.lb_common.dto.AuthUserDTO;
import com.pulse_gym.lb_common.dto.DocumentoLegalRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.user.DocumentoLegal;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.enums.EnumEstadoDocumentoLegal;
import com.pulse_gym.lb_common.enums.EnumRol;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.repository.DocumentoLegalRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentoLegalService {


    /** RestTemplate para llamar a auth-service */
    private final RestTemplate restTemplate;

    /** URL del servicio de autenticación */
    private final String authServiceUrl = "http://pg-ms-auth/auth";

    /** Repositorio de documentos legales */
    private final DocumentoLegalRepository documentoLegalRepository;

    /** Repositorio de perfiles de usuario */
    private final UsuarioPerfilRepository usuarioRepository;

    /**
     * Obtiene el rol de un usuario desde auth-service
     * 
     * @param email Email del usuario
     * @return Rol del usuario o null si no se encuentra
     */
    private EnumRol obtenerRolDesdeAuth(String email) {
        try {
            ResponseEntity<AuthUserDTO> authResponse = restTemplate.getForEntity(
                    authServiceUrl + "/api/internal/users/email/" + email,
                    AuthUserDTO.class);
            
            if (authResponse.getBody() != null && authResponse.getBody().getRol() != null) {
                return authResponse.getBody().getRol();
            }
        } catch (Exception e) {
            System.err.println("Error al obtener rol para " + email + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Carga un documento legal para un usuario específico. Solo los usuarios con rol ADMIN o RECEPCIONISTA pueden realizar esta acción, y el documento solo puede ser cargado para usuarios con rol SOCIO.
     * @param requestDTO DTO con los datos del documento legal a cargar
     * @param userRol Rol del usuario que realiza la acción (obtenido del token de autenticación)
     * @return Mensaje de éxito o error en la carga del documento legal
     */
    @Transactional
    public MessegeGlobalDTO cargarDocumentoLegal(DocumentoLegalRequestDTO requestDTO, String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

        UsuarioPerfil usuario = usuarioRepository.findById(requestDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + requestDTO.getIdUsuario()));

        EnumRol rolSocio = obtenerRolDesdeAuth(usuario.getEmail());
        
        if (rolSocio == null) {
            throw new RuntimeException("No se pudo verificar el rol del usuario");
        }
        
        if (rolSocio != EnumRol.socio) {
            throw new RuntimeException("Solo se pueden cargar documentos legales para socios. Rol actual: " + rolSocio);
        }

        DocumentoLegal documento = new DocumentoLegal();
        documento.setUsuario(usuario);
        documento.setTipoDocumento(requestDTO.getTipoDocumento());
        documento.setUrlArchivoFirmado(requestDTO.getUrlArchivoFirmado());
        documento.setEstado(EnumEstadoDocumentoLegal.VIGENTE);

        documentoLegalRepository.save(documento);
        return new MessegeGlobalDTO("Documento legal cargado correctamente");
    }
}
