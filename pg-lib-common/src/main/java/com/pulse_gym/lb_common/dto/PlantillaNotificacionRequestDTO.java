package com.pulse_gym.lb_common.dto;

import java.util.Set;

import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlantillaNotificacionRequestDTO {

    /**
     * Nombre de la plantilla de notificacion
     */
    @NotBlank(message = "El nombre de la plantilla es obligatorio")
    private String nombre;

    /** Titulo de la notificacion */
    @NotBlank(message = "El titulo de la plantilla es obligatorio")
    private String titulo;
    /**
     * Descripcion de la plantilla de notificacion
     */  
    @NotBlank(message = "La descripcion de la plantilla es obligatoria")
    private String descripcion;

    /**
     * Contenido de la notificacion
     */
    @NotBlank(message = "El contenido de la plantilla es obligatorio")
    private String contenido;

    /**
     * Plantilla que pertenece al canal seleccionado            
     */
    @NotNull(message = "El tipo de plantilla es obligatorio")
    private EnumCanalNotificacion tipoPlantilla;

    /**
     * Que tipo (evento) de notificacion esta siendo mandado
     */
    private EnumEventoAsociado eventoAsociado;

    /**
     * Eventos asociados a la plantilla
     */
    @NotEmpty(message = "Debe indicar al menos un evento asociado")
    private Set<EnumEventoAsociado> eventosAsociados;

    /**
     * Estado de la plantilla (Activa/Inactiva) True/False
     */
    private Boolean estado;
}
