package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BiometricLoginRequestDTO {
    
    @NotBlank(message = "El token es obligatorio")
    private String token;
}