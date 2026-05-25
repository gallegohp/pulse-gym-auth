// ProveedorResponseDTO.java
package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class ProveedorResponseDTO {

    /**
     * Identificador único del proveedor
     */
    private Long idProveedor;
    /**
     * Nombre de la empresa
     */
    private String nombreEmpresa;
    /**
     * Nombre de contacto
     */
    private String contactoNombre;
    /**
     * Teléfono
     */
    private String telefono;
    /**
     * Correo electrónico
     */
    private String email;
    /**
     * Cantidad de equipos
     */
    private Integer cantidadEquipos;
}