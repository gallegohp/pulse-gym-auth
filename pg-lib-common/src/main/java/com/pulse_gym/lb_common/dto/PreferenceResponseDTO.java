package com.pulse_gym.lb_common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceResponseDTO {
    private String preferenceId;
    private String initPoint; // URL de Mercado Pago Sandbox para pagar
}