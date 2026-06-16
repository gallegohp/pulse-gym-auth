package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PagoResponseDTO {

    /** Identificador único del pago */
    private Long idPago;

    /** ID del socio que realizó el pago */
    private Long idSocio;

    /** Nombre completo del socio */
    private String nombreSocio;

    /** Correo electrónico del socio */
    private String emailSocio;

    /** ID de la membresía asignada asociada al pago */
    private Long idSocioMembresia;

    /** Nombre de la membresía pagada */
    private String nombreMembresia;

    /** Monto del pago */
    private BigDecimal monto;

    /** Fecha y hora en que se realizó el pago */
    private LocalDateTime fechaPago;

    /** Método de pago utilizado */
    private String metodoPago;

    /** Número de comprobante o referencia del pago */
    private String numeroComprobante;

    /** ID del administrador que registró el pago */
    private Long idAdminRegistro;

    /** Nombre del administrador que registró el pago */
    private String nombreAdminRegistro;

    /** Observaciones adicionales sobre el pago */
    private String observaciones;

    /** Indica si el pago está anulado */
    private Boolean anulado;

    /** Motivo de la anulación del pago */
    private String motivoAnulacion;

    /** Fecha y hora en que se anuló el pago */
    private LocalDateTime fechaAnulacion;
}