package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DetalleRutinaResponseDTO {

    /** ID del detalle de la rutina */
    private Long idDetalle;

    /** ID del ejercicio asociado */
    private Long idEjercicio;

    /** Nombre del ejercicio */
    private String nombreEjercicio;

    /** Grupo muscular del ejercicio */
    private String grupoMuscular;

    /** URL de la imagen del ejercicio */
    private String urlImagen;

    /** URL del video del ejercicio */
    private String urlVideo;

    /** Día de la semana (1-7) en que se realiza */
    private Integer diaSemana;

    /** Orden de ejecución dentro del día */
    private Integer orden;

    /** Número de series a realizar */
    private Integer series;

    /** Número mínimo de repeticiones */
    private Integer repeticionesMin;

    /** Número máximo de repeticiones */
    private Integer repeticionesMax;

    /** Peso sugerido para el ejercicio */
    private BigDecimal pesoSugerido;

    /** Tiempo de descanso en segundos entre series */
    private Integer descansoSegundos;

    /** Notas o recomendaciones adicionales */
    private String notas;

    /** Usuario que modificó el detalle */
    private String modificadoPor;
}