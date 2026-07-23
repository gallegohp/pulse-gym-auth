package com.pulse_gym.lb_common.entity.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ejercicio")
@Data
public class Ejercicio {

    /** Identificador único del ejercicio */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ejercicio")
    private Long idEjercicio;

    /** Nombre del ejercicio */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Grupo muscular: PECHO, ESPALDA, PIERNA, HOMBRO, BRAZO, CORE, CARDIO
     */
    @Column(name = "grupo_muscular", nullable = false, length = 50)
    private String grupoMuscular;

    /**
     * Equipo necesario (texto descriptivo del equipo)
     */
    @Column(name = "equipo_necesario", length = 100)
    private String equipoNecesario;

    /**
     * Explicación técnica del ejercicio
     */
    @Column(name = "explicacion_tecnica", columnDefinition = "TEXT")
    private String explicacionTecnica;

    /**
     * URL de la imagen demostrativa
     */
    @Column(name = "url_imagen", length = 255)
    private String urlImagen;

    /**
     * Dificultad: 1-5 (opcional, se añade por completitud)
     */
    @Column(name = "dificultad")
    private Integer dificultad;

    /**
     * Calorías por minuto (opcional)
     */
    @Column(name = "calorias_por_minuto", precision = 5, scale = 2)
    private java.math.BigDecimal caloriasPorMinuto;

    /** Indica si el ejercicio está activo */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /** Fecha de creación del registro */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    /** Establece la fecha de creación antes de persistir */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}