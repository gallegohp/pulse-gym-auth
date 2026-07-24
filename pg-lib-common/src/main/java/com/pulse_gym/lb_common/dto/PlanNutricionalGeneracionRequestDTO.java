package com.pulse_gym.lb_common.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlanNutricionalGeneracionRequestDTO {

    /** ID del socio al que se generará el plan nutricional */
    @NotNull(message = "El ID del socio es obligatorio")
    @JsonProperty("id_socio") 
    private Long idSocio;

    /** ID de la rutina asociada (opcional) */
    private Long idRutina;

    /** Restricciones dietéticas del socio (ej: vegetariano, vegano, sin gluten) */
    private List<String> restriccionesDieteticas;

    /** Alergias alimentarias del socio */
    private List<String> alergias;

    /** Intolerancias alimentarias del socio */
    private List<String> intolerancias;

    /** Objetivo específico del plan (ej: ganar masa, perder peso, mantener) */
    private String objetivoEspecifico;
}