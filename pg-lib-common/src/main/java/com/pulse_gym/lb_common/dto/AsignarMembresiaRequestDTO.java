package com.pulse_gym.lb_common.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignarMembresiaRequestDTO {

    /** ID del socio */
    @NotNull(message = "El ID del socio es obligatorio")
    private Long idSocio;

    /** ID de la membresía */
    @NotNull(message = "El Id de la membresia es obligatoria")
    private Long idMembresia;

    /** Fecha de inicio de la membresía */
    private LocalDate fechaInicio;

    /** Observaciones */
    private String observaciones;
}
