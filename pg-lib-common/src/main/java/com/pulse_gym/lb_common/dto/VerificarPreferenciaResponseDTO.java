package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class VerificarPreferenciaResponseDTO {

    /**
     * Indica si el envio esta permitido segun preferencias y limites
     */
    private boolean permitido;

    /**
     * Motivo cuando el envio no esta permitido
     */
    private String motivo;
}
