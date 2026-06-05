package com.pulse_gym.lb_common.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.pulse_gym.lb_common.dto.AuthUserDTO;

@FeignClient(name = "pg-ms-auth", url = "${microservicio.auth.url:http://pg-ms-auth:8090}")
public interface AuthClient {
    
    @GetMapping("/auth/api/internal/users/{id}")
    AuthUserDTO obtenerUsuarioPorId(@PathVariable("id") Long id);
}