// ReporteFallaDTO.java
package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteFallaDTO {
    
    @NotNull(message = "La urgencia es obligatoria")
    private String urgencia; // BAJA, MEDIA, ALTA, CRITICA
    
    @NotBlank(message = "La descripción de la falla es obligatoria")
    private String descripcion;
}