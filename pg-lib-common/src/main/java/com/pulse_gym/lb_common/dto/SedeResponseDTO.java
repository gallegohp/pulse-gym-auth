package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class SedeResponseDTO {
    private Long idSede;
    private String nombreSede;
    private String direccion;
    private String telefono;
    private String ciudad;
    private Integer cantidadEquipos;
}