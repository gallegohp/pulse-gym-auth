package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistroHuellaRequestDTO {
    @NotBlank(message = "El deviceId es obligatorio")
    private String deviceId;
}
