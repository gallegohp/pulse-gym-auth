package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PerfilMedicoRequestDTO {

    /** ID del socio */
    @NotNull(message = "El ID del socio es obligatorio")
    private Long idSocio;

    /** Peso en kilogramos */
    @DecimalMin(value = "0.0", inclusive = false, message = "El peso debe ser mayor a 0")
    private BigDecimal pesoKg;

    /** Estatura en centímetros */
    @DecimalMin(value = "0", inclusive = false, message = "La estatura debe ser mayor a 0")
    private Short estaturaCm;

    /** Alergias */
    private String alergias;

    /** Condiciones crónicas */
    private String condicionesCronicas;

    /** Lesiones previas */
    private String lesionesPrevias;

    /** Porcentaje de grasa */
    @DecimalMin(value = "0.0", message = "El porcentaje de grasa debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje de grasa no puede superar 100")
    private BigDecimal porcentajeGrasa;
}