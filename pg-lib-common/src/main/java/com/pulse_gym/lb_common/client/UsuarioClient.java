package com.pulse_gym.lb_common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pulse_gym.lb_common.dto.UsuarioPerfilResponseDTO;

@FeignClient(name = "pg-ms-users", url = "${microservicio.usuarios.url:http://pg-ms-users:8081}")
public interface UsuarioClient {
    
    @GetMapping("/api/v1/usuarios/{idUsuario}")
    UsuarioPerfilResponseDTO obtenerUsuarioPorId(@PathVariable("idUsuario") Long idUsuario);
}