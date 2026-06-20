package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SuspenderMembresiaRequestDTO {
    
    /** ID de la asignación de membresía */
    @NotNull(message = "El ID de la asignación de membresía es obligatorio")
    private Long idSocioMembresia;
    
    /** Motivo de suspensión */
    @NotBlank(message = "El motivo de suspensión es obligatorio")
    private String motivo;
}