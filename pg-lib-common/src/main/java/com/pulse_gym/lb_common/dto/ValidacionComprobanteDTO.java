package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ValidacionComprobanteDTO {

    /** ID del pago */
    private Long idPago;

    /** Nombre del socio */
    private String socioNombre;

    /** Email del socio */
    private String socioEmail;

    /** Nombre de la membresía */
    private String membresiaNombre;

    /** Monto del pago */
    private BigDecimal monto;

    /** Fecha del pago */
    private LocalDateTime fechaPago;

    /** Método de pago utilizado */
    private String metodoPago;

    /** Número de comprobante */
    private String numeroComprobante;

    /** Estado del pago */
    private String estado;

    /** Indica si el pago está anulado */
    private Boolean anulado;

    /** Motivo de la anulación */
    private String motivoAnulacion;

    /** Indica si el comprobante es válido */
    private Boolean valido;

    /** Mensaje descriptivo */
    private String mensaje;
}