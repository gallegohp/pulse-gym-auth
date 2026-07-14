package com.pulse_gym.lb_common.entity.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "detalle_rutina")
@Data
public class DetalleRutina {

    /** Identificador único del detalle de la rutina */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_rutina")
    private Long idDetalleRutina;

    /** Rutina a la que pertenece este detalle */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_rutina_ia", nullable = false)
    private RutinaIA rutinaIa;

    /** Ejercicio asignado en este detalle */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_ejercicio", nullable = false)
    private Ejercicio ejercicio;

    /** Número de series a realizar */
    @Column(name = "series", nullable = false)
    private Integer series;

    /** Número mínimo de repeticiones */
    @Column(name = "repeticiones_min")
    private Integer repeticionesMin;

    /** Número máximo de repeticiones */
    @Column(name = "repeticiones_max")
    private Integer repeticionesMax;

    /** Peso sugerido para el ejercicio */
    @Column(name = "peso_sugerido", precision = 10, scale = 2)
    private BigDecimal pesoSugerido;

    /** Tiempo de descanso en segundos entre series */
    @Column(name = "descanso_segundos")
    private Integer descansoSegundos;

    /** Día de la semana (1-7) en que se realiza el ejercicio */
    @Column(name = "dia_semana")
    private Integer diaSemana;

    /** Orden de ejecución dentro del día */
    @Column(name = "orden")
    private Integer orden;

    /** Notas o recomendaciones adicionales */
    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    /** Usuario que modificó el detalle (entrenador o IA) */
    @Column(name = "modificado_por", length = 100)
    private String modificadoPor;

    /** Fecha de la última modificación */
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
}