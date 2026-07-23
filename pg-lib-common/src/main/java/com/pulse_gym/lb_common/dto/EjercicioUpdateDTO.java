package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EjercicioUpdateDTO {

    /** Nombre del ejercicio */
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    /** Grupo muscular al que pertenece el ejercicio */
    @Size(max = 50, message = "El grupo muscular no puede superar los 50 caracteres")
    private String grupoMuscular;

    /** Equipo necesario para realizar el ejercicio */
    @Size(max = 100, message = "El equipo necesario no puede superar los 100 caracteres")
    private String equipoNecesario;

    /** Explicación técnica del ejercicio */
    private String explicacionTecnica;

    /** URL de la imagen demostrativa */
    @Size(max = 255, message = "La URL de la imagen no puede superar los 255 caracteres")
    private String urlImagen;

    /** Nivel de dificultad del ejercicio (1-5) */
    @Min(value = 1, message = "La dificultad mínima es 1")
    @Max(value = 5, message = "La dificultad máxima es 5")
    private Integer dificultad;

    /** Calorías estimadas quemadas por minuto */
    @DecimalMin(value = "0.0", message = "Las calorías por minuto no pueden ser negativas")
    private BigDecimal caloriasPorMinuto;

    /** Indica si el ejercicio está activo */
    private Boolean activo;
}