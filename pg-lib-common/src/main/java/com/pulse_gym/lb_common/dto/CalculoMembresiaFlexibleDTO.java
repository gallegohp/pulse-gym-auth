package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CalculoMembresiaFlexibleDTO {
    
    /** El ID de la membresía flexible */
    @NotNull(message = "El ID de la membresía flexible es obligatorio")
    private Long idMembresia;
    
    /** La cantidad de días */
    @NotNull(message = "La cantidad de días es obligatoria")
    @Min(value = 1, message = "La cantidad de días debe ser al menos 1")
    private Integer cantidadDias;
    
    /** Indica si la membresía incluye IA */
    @NotNull(message = "Debe indicar si incluye IA o no")
    private Boolean incluyeIA;
}