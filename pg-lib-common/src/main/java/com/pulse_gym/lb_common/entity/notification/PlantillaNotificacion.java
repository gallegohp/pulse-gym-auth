package com.pulse_gym.lb_common.entity.notification;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "plantilla_notificacion")
@Entity
@Data
public class PlantillaNotificacion {

    /**
     * Identificador de la plantilla de notificacion
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_plantilla")
    private Long idPlantilla;

    /**
     * Nombre de la plantilla de notificacion
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;

    /**
     * Descripcion de la plantilla de notificacion
     */
    @Column(name = "descripcion", nullable = true)
    private String descripcion;

    /**
     * Contenido de la notificacion
     */
    @Column(name = "contenido", nullable = false)
    private String contenido;

    /**
     * Plantilla que pertenece al canal seleccionado
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumCanalNotificacion tipoPlantilla;

    /**
     * Que tipo (evento) de notificacion esta siendo mandado
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "evento_asociado", nullable = false)
    private EnumEventoAsociado eventoAsociado;

    /**
     * Estado de la plantilla (Activa/Inactiva) True/False
     */
    @Column(name = "estado", nullable = false)
    private Boolean estado;

    /**
     * fecha de creacion de la plantilla
     */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;


}


