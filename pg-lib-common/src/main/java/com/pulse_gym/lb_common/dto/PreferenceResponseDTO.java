package com.pulse_gym.lb_common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PreferenceResponseDTO {

    /** ID de la preferencia creada en MercadoPago */
    private String preferenceId;

    /** URL de inicio de pago en MercadoPago (sandbox o producción) */
    private String initPoint;
}