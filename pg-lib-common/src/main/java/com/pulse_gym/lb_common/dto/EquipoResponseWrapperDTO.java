package com.pulse_gym.lb_common.dto;

import java.util.List;

import com.pulse_gym.lb_common.entity.operation.Equipo;

import lombok.Data;

@Data
public class EquipoResponseWrapperDTO {

    /** Indica si la operación fue exitosa */
    private Boolean success;

    /** Mensaje descriptivo de la operación */
    private String message;

    /** Cantidad de equipos encontrados */
    private Integer count;

    /** Lista de equipos */
    private List<Equipo> data;
}
