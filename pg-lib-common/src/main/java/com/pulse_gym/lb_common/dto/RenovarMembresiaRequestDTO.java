package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RenovarMembresiaRequestDTO {
    
    /** ID de la asignación de membresía */
    @NotNull(message = "El ID de la asignación de membresía es obligatorio")
    private Long idSocioMembresia;
    
    /** Observaciones */
    private String observaciones;
}
