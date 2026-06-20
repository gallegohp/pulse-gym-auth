package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import com.pulse_gym.lb_common.enums.EnumMetodoPago;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistrarPagoRequestDTO {

    /** ID de la membresía asignada al socio a la que se aplicará el pago */
    @NotNull(message = "El ID de la membresía asignada es obligatorio")
    private Long idSocioMembresia;

    /** Monto del pago (Ahora es opcional en el JSON, se calcula en el Backend) */
    private BigDecimal monto;

    /** Método de pago */
    @NotNull(message = "El método de pago es obligatorio")
    private EnumMetodoPago metodoPago;

    /** Número de comprobante o referencia del pago (opcional) */
    private String numeroComprobante;

    /** Observaciones adicionales sobre el pago (opcional) */
    private String observaciones;
}