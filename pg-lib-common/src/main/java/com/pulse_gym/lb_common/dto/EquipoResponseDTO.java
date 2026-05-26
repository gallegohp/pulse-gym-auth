package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class EquipoResponseDTO {
    /**
     *  id del equipo
     */
    private Long idEquipo;

    /**
     * nombre del equipo
     */
    private String nombreEquipo;

    /**
     * descripcion del equipo
     */
    private String descripcion;

    /**
     * estado del equipo
     */
    private String estado;

    /**
     * id de la sede a la que pertenece el equipo
     */
    private Long idSede;

    /**
     * id del proveedor del equipo
     */
    private Long idProveedor;

}
