package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarPagoRequestDTO {

    /** ID de la membresía asignada al socio a la que se aplicará el pago */
    @NotNull(message = "El ID de la membresía asignada es obligatorio")
    private Long idSocioMembresia;

    /** Monto del pago a realizar */
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    /**
     * Método de pago (EFECTIVO, TRANSFERENCIA_BANCOLOMBIA, TARJETA_CREDITO,
     * TARJETA_DEBITO, OTRO)
     */
    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago;

    /** Número de comprobante o referencia del pago (opcional) */
    private String numeroComprobante;

    /** Observaciones adicionales sobre el pago (opcional) */
    private String observaciones;
}