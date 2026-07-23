package com.pulse_gym.lb_common.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RutinaHistorialResponseDTO {

    /** ID del historial */
    private Long idHistorial;

    /** Versión de la rutina en este historial */
    private Integer version;

    /** Datos de la rutina en formato JSON */
    private String datosJson;

    /** Usuario que realizó la modificación */
    private String modificadoPor;

    /** Motivo de la modificación */
    private String motivo;

    /** Fecha de la modificación */
    private LocalDateTime fechaModificacion;
}