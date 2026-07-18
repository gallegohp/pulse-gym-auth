package com.pulse_gym.lb_common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;

@FeignClient(name = "pg-ms-users", url = "${microservicio.usuarios.url:http://pg-ms-users:8081}", contextId = "clienteGeneral")
public interface UsuarioClient {
    
    @GetMapping("/api/v1/usuarios/{idUsuario}")
    UsuarioPerfilResponseDTO obtenerUsuarioPorId(@PathVariable("idUsuario") Long idUsuario);

    /**
     * Obtiene el perfil de usuario por email para integracion entre microservicios
     */
    @GetMapping("/api/internal/usuarios/email/{email}")
    UsuarioPerfilResponseDTO obtenerUsuarioPorEmail(@PathVariable("email") String email);

    /**
     * Creamos un nuevo endpoint el cual no pasa por el gateway (comunicacion directa)
     * @param idUsuario
     * @return
     */
    @GetMapping("/api/internal/usuarios/{idUsuario}")
    UsuarioPerfilResponseDTO obtenerUsuarioPorIdInterno(@PathVariable("idUsuario") Long idUsuario);
}