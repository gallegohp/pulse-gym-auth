package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RutinaAjusteRequestDTO {

    /** ID del detalle de la rutina a modificar */
    @NotNull(message = "El ID del detalle es obligatorio")
    private Long idDetalle;

    /** Número de series */
    private Integer series;

    /** Número mínimo de repeticiones */
    @Min(value = 0, message = "Las repeticiones mínimas no pueden ser negativas")
    private Integer repeticionesMin;

    /** Número máximo de repeticiones */
    @Min(value = 0, message = "Las repeticiones máximas no pueden ser negativas")
    private Integer repeticionesMax;

    /** Peso sugerido para el ejercicio */
    private BigDecimal pesoSugerido;

    /** Tiempo de descanso en segundos entre series */
    @Min(value = 0, message = "El descanso no puede ser negativo")
    private Integer descansoSegundos;

    /** Notas o recomendaciones adicionales */
    private String notas;

    /** Motivo de la modificación */
    private String motivo;
}
