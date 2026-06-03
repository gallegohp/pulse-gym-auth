package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PerfilMedicoResponseDTO {

    /** ID del perfil médico */
    private Long idPerfilMedico;

    /** ID del socio */
    private Long idSocio;

    /** Nombre del socio */
    private String nombreSocio;

    /** Peso en kilogramos */
    private BigDecimal pesoKg;

    /** Estatura en centímetros */
    private Short estaturaCm;

    /** Alergias */
    private String alergias;

    /** Condiciones crónicas */
    private String condicionesCronicas;

    /** Lesiones previas */
    private String lesionesPrevias;

    /** Porcentaje de grasa */
    private BigDecimal porcentajeGrasa;

    /** Fecha de actualización */
    private LocalDateTime fechaActualizacion;
}