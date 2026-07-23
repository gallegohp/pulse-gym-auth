package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleRutinaResponseDTO {

    /** ID del detalle de la rutina */
    private Long idDetalle;

    /** ID del ejercicio asociado */
    private Long idEjercicio;

    /** Nombre del ejercicio */
    @JsonProperty("nombreEjercicio")
    private String nombreEjercicio;

    /** Grupo muscular del ejercicio */
    @JsonProperty("grupoMuscular")
    private String grupoMuscular;

    /** URL de la imagen del ejercicio */
    @JsonProperty("urlImagen")
    private String urlImagen;

    /** Día de la semana (1-7) en que se realiza */
    @JsonProperty("diaSemana")
    private Integer diaSemana;

    /** Orden de ejecución dentro del día */
    private Integer orden;

    /** Número de series a realizar */
    private Integer series;

    /** Número mínimo de repeticiones */
    @JsonProperty("repeticionesMin")
    private Integer repeticionesMin;

    /** Número máximo de repeticiones */
    @JsonProperty("repeticionesMax")
    private Integer repeticionesMax;

    /** Peso sugerido para el ejercicio */
    @JsonProperty("pesoSugerido")
    private BigDecimal pesoSugerido;

    /** Tiempo de descanso en segundos entre series */
    @JsonProperty("descansoSegundos")
    private Integer descansoSegundos;

    /** Notas o recomendaciones adicionales */
    private String notas;

    /** Usuario que modificó el detalle */
    private String modificadoPor;
}