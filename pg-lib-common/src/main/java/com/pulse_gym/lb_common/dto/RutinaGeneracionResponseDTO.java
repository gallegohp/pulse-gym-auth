package com.pulse_gym.lb_common.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RutinaGeneracionResponseDTO {

    /** ID de la rutina */
    private Long idRutina;

    /** Nombre de la rutina */
    private String nombre;

    /** Descripción de la rutina */
    private String descripcion;

    /** Explicación generada por IA sobre la rutina */
    @JsonProperty("explicacionIA")
    private String explicacionIA;

    /** Versión de la rutina */
    private Integer version;

    /** Indica si fue generada por IA */
    private Boolean generadaPorIA;

    /** Fecha de generación de la rutina */
    private LocalDateTime fechaGeneracion;

    /** Lista de detalles (ejercicios) de la rutina */
    @JsonProperty("detalles")
    private List<DetalleRutinaResponseDTO> detalles;
}