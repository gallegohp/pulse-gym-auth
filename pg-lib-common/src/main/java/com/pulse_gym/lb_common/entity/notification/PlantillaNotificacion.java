package com.pulse_gym.lb_common.entity.notification;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plantilla")
    private Long idPlantilla;

    /**
     * Nombre de la plantilla de notificacion
     */
    @Column(name = "nombre", nullable = false)
    private String nombre;


    /** Titulo de la notificacion */
    @Column(name = "titulo")
    private String titulo;

    /**
     * Descripcion de la plantilla de notificacion
     */
    @Column(name = "descripcion")
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
     * Eventos asociados a la plantilla para envios automaticos
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "plantilla_evento", joinColumns = @JoinColumn(name = "id_plantilla"))
    @Column(name = "evento")
    private Set<EnumEventoAsociado> eventosAsociados = new HashSet<>();

    /**
     * Indica si la plantilla fue eliminada mediante soft delete
     */
    @Column(name = "eliminada", nullable = false)
    private Boolean eliminada = false;

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
