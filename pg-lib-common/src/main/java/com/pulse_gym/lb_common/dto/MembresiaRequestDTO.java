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
    
    /** El precio total de la membresía */
    @NotNull(message = "El precio total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo")
    private BigDecimal precioTotal;
    
    /** La duración de la membresía en meses */
    @Min(value = 1, message = "La duración debe ser al menos 1")
    private Integer duracionMeses = 1;
    
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
    
    /** Indica si la membresía está activa */
    private Boolean activo = true;
}