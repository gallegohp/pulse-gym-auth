package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class MembresiaResponseDTO {

    /** El ID de la membresía */
    private Long idMembresia;

    /** El nombre de la membresía */
    private String nombre;

    /** El precio total de la membresía */
    private BigDecimal precioTotal;

    /** La duración de la membresía en meses */
    private Integer duracionMeses;

    /** El tipo de duración */
    private String tipoDuracion;

    /** Indica si la membresía incluye IA */
    private Boolean incluyeIA;

    /** Indica si la membresía es flexible */
    private Boolean esFlexible;

    /** El precio por día */
    private BigDecimal precioPorDia;

    /** Indica si la membresía está activa   */
    private Boolean activo;
}