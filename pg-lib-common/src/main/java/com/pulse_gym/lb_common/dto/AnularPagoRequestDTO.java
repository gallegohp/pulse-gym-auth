package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnularPagoRequestDTO {

    /** ID del pago que se desea anular */
    @NotNull(message = "El ID del pago es obligatorio")
    private Long idPago;

    /** Motivo por el cual se anula el pago */
    @NotBlank(message = "El motivo de anulación es obligatorio")
    private String motivo;
}
