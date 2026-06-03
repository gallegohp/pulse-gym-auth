package com.pulse_gym.lb_common.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CertificacionResponseDTO {

    /** ID de la certificación */
    private Long idCertificacion;

    /** ID del entrenador al que se asociará la certificación */
    private Long idEntrenador;

    /** Nombre del entrenador */
    private String nombreEntrenador;

    /** Nombre de la certificación */
    private String nombreCertificacion;

    /** URL del PDF de la certificación */
    private String urlPdf;

    /** Fecha de subida de la certificación */
    private LocalDateTime fechaSubida;
}