package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContrasenaOlvidada {

    /**El nombre del usuario */
    @NotBlank(message = "El username es obligatorio")
    private String username;
}
