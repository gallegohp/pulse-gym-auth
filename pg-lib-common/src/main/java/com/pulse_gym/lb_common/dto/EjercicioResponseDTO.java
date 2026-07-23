package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EjercicioResponseDTO {

    /** Identificador único del ejercicio */
    private Long idEjercicio;

    /** Nombre del ejercicio */
    private String nombre;

    /** Grupo muscular al que pertenece el ejercicio */
    private String grupoMuscular;

    /** Equipo necesario para realizar el ejercicio */
    private String equipoNecesario;

    /** Explicación técnica del ejercicio */
    private String explicacionTecnica;

    /** URL de la imagen demostrativa */
    private String urlImagen;

    /** Nivel de dificultad del ejercicio (1-5) */
    private Integer dificultad;

    /** Calorías estimadas quemadas por minuto */
    private BigDecimal caloriasPorMinuto;

    /** Indica si el ejercicio está activo */
    private Boolean activo;
}