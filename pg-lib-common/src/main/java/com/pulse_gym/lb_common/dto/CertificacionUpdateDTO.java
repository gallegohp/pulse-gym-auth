package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CertificacionUpdateDTO {

    /** ID de la certificación a actualizar */
    @NotBlank(message = "El nombre de la certificación es obligatorio")
    private String nombre;

    /** URL del PDF de la certificación */
    @NotBlank(message = "La URL del PDF es obligatoria")
    private String urlPdf;
}