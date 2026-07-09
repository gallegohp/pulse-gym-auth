package com.pulse_gym.lb_common.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FiltroPagosRequestDTO {

    /** ID del socio para filtrar pagos (opcional) */
    private Long idSocio;

    /**
     * Método de pago para filtrar (EFECTIVO, TRANSFERENCIA_BANCOLOMBIA,
     * TARJETA_CREDITO, TARJETA_DEBITO, OTRO)
     */
    private String metodoPago;

    /** Filtra pagos anulados o no anulados (opcional) */
    private Boolean anulado;

    /** Fecha de inicio para filtrar pagos desde una fecha específica (opcional) */
    private LocalDateTime fechaInicio;

    /** Fecha de fin para filtrar pagos hasta una fecha específica (opcional) */
    private LocalDateTime fechaFin;
}
