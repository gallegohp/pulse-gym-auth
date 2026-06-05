package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MembresiaRequestDTO {

    /** El nombre de la membresía */
    @NotBlank(message = "El nombre de la membresía es obligatorio")
    private String nombre;

    /** La cantidad de unidades */
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad = 1;

    /** El tipo de duración */
    @NotBlank(message = "El tipo de duración es obligatorio")
    private String tipoDuracion;

    /** Indica si la membresía incluye IA */
    @NotNull(message = "Debe indicar si incluye IA o no")
    private Boolean incluyeIA;

    /** Indica si la membresía es flexible */
    @NotNull(message = "Debe indicar si es membresía flexible")
    private Boolean esFlexible;

    /** El precio por día */
    @DecimalMin(value = "0.0", message = "El precio por día no puede ser negativo")
    private BigDecimal precioPorDia;

    /** Los beneficios de la membresía */
    private String beneficios;

    /** Las restricciones de la membresía */    
    private String restricciones;

    /** Indica si la membresía está activa */
    private Boolean activo = true;
}