package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HistorialFisicoResponseDTO {

    /** ID del historial físico */
    private Long idHistorialFisico;

    /** ID del socio */
    private Long idSocio;

    /** Nombre del socio */
    private String nombreSocio;

    /** ID del recepcionista */
    private Long idRecepcionista;

    /** Nombre del recepcionista */
    private String nombreRecepcionista;

    /** Fecha de la medición */
    private LocalDateTime fechaMedicion;

    /** Peso en kg */
    private BigDecimal pesoKg;

    /** Porcentaje de grasa */
    private BigDecimal porcentajeGrasa;

    /** Porcentaje de músculo */
    private BigDecimal porcentajeMusculo;

    /** Cintura en cm */
    private BigDecimal cinturaCm;

    /** Pecho en cm */
    private BigDecimal pechoCm;

    /** Brazo izquierdo en cm */
    private BigDecimal brazoIzqCm;

    /** Brazo derecho en cm */
    private BigDecimal brazoDerCm;

    /** Pierna izquierda en cm */
    private BigDecimal piernaIzqCm;

    /** Pierna derecha en cm */
    private BigDecimal piernaDerCm;
}
