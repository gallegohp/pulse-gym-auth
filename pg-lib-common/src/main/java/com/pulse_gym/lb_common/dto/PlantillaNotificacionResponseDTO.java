package com.pulse_gym.lb_common.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PlantillaNotificacionResponseDTO {

    /**
     * Identificador de la plantilla de notificacion
     */
    private Long idPlantilla;

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
    private Long tipoPlantilla;

    /**
     * Que tipo (evento) de notificacion esta siendo mandado
     */
    private Long eventoAsociado;

    /**
     * Estado de la plantilla (Activa/Inactiva) True/False            
     */
    private Boolean estado;

    /**
     * fecha de creacion de la plantilla
     */
    private LocalDateTime fechaCreacion;    
}