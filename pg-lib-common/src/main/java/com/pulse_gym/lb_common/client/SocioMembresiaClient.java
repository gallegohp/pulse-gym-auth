package com.pulse_gym.lb_common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pulse_gym.lb_common.dto.EstadoMembresiaResponseDTO;

@FeignClient(name = "pg-ms-users", url = "${microservicio.usuarios.url:http://pg-ms-users:8081}", contextId = "manejoMembrsia")
public interface SocioMembresiaClient {
    
    /**
     * Consulta el estado de la membresia de un socio para control del biometrico
     * Endpoint interno expuesto por ms-users
     * 
     * @param idSocio Id del socio
     * @return dto con el estado de la membresia
     */
    @GetMapping("/api/internal/socios-membresias/biometrico/{idSocio}")
    EstadoMembresiaResponseDTO consultarEstadoBiometrico(@PathVariable("idSocio") Long idSocio);
}