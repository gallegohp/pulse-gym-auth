package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CertificacionRequestDTO {

    /** ID del entrenador al que se asociará la certificación */
    @NotNull(message = "El ID del entrenador es obligatorio")
    private Long idEntrenador;

    /** Nombre de la certificación */
    @NotBlank(message = "El nombre de la certificación es obligatorio")
    private String nombre;

    /** URL del PDF de la certificación */
    @NotBlank(message = "La URL del PDF es obligatoria")
    private String urlPdf;
}
