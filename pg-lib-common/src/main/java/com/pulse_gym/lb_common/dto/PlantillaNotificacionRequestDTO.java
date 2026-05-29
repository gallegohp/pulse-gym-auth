package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class PlantillaNotificacionRequestDTO {
    /**
     * Nombre de la plantilla de notificacion
     */
    private String nombre;

    /**
     * Descripcion de la plantilla de notificacion
     */                                 
    private String descripcion;

    /**
     * Contenido de la notificacion
     */
    private String contenido;

    /**
     * Plantilla que pertenece al canal seleccionado            
     */
    private String tipoPlantilla;

    /**
     * Que tipo (evento) de notificacion esta siendo mandado
     */
    private String eventoAsociado;

    /**
     * Estado de la plantilla (Activa/Inactiva) True/False
     */
    private Boolean estado;
}
