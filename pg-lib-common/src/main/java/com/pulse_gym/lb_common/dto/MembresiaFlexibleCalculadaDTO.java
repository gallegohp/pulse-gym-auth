package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MembresiaFlexibleCalculadaDTO {

    /** El ID de la membresía flexible */
    private Long idMembresia;

    /** El nombre de la membresía flexible */
    private String nombre;

    /** La cantidad de días */
    private Integer cantidadDias;

    /** El precio por día */
    private BigDecimal precioPorDia;

    /** El precio total calculado */
    private BigDecimal precioTotalCalculado;

    /** Indica si la membresía incluye IA */
    private Boolean incluyeIA;
}