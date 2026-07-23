package com.pulse_gym.lb_common.dto;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RutinaGeneracionRequestDTO {

    /** ID del socio al que se generará la rutina */
    @NotNull(message = "El ID del socio es obligatorio")
    private Long idSocio;

    /** Días por semana para la rutina */
    @Min(value = 1, message = "Los días por semana deben ser al menos 1")
    private Integer diasPorSemana;

    /** Duración en semanas de la rutina */
    @Min(value = 1, message = "La duración en semanas debe ser al menos 1")
    private Integer duracionSemanas;

    /** Equipamiento preferido para la rutina */
    private List<String> preferenciasEquipamiento;

    /** Ejercicios a evitar por lesiones o preferencias */
    private List<String> evitarEjercicios;

    /** Grupos musculares a priorizar */
    private List<String> preferenciasGruposMusculares;

    /** Objetivo específico de la rutina */
    private String objetivoEspecifico;

    /** Indica si se debe incluir cardio en la rutina */
    private Boolean incluirCardio = true;
}