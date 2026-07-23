package com.pulse_gym.lb_common.entity.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "rutina_ia")
@Data
public class RutinaIA {

    /** Identificador único de la rutina generada por IA */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rutina_ia")
    private Long idRutinaIa;

    /** Socio al que pertenece la rutina */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_socio", nullable = false)
    private UsuarioPerfil socio;

    /** Entrenador que supervisa la rutina (opcional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_entrenador")
    private UsuarioPerfil entrenador;

    /** Objetivo de la rutina (ej: HIPERTROFIA, DEFINICION, RESISTENCIA) */
    @Column(name = "objetivo", length = 100)
    private String objetivo;

    /** Nivel del usuario (ej: PRINCIPIANTE, INTERMEDIO, AVANZADO) */
    @Column(name = "nivel", length = 50)
    private String nivel;

    /** Condiciones especiales del usuario (ej: lesiones, restricciones) */
    @Column(name = "condiciones", length = 255)
    private String condiciones;

    /** Rutina generada en formato estructurado (JSON/Texto) */
    @Column(name = "rutina_generada", columnDefinition = "TEXT")
    private String rutinaGenerada;

    /** Fecha de generación de la rutina */
    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    /** Modelo de IA utilizado para la generación */
    @Column(name = "modelo_ia", length = 50)
    private String modeloIa;

    /** Versión de la rutina (para control de cambios) */
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    /** Indica si la rutina está activa */
    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    /** Explicación generada por IA sobre la rutina */
    @Column(name = "explicacion_ia", columnDefinition = "TEXT")
    private String explicacionIa;

    /** Detalles de los ejercicios que componen la rutina */
    @OneToMany(mappedBy = "rutinaIa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleRutina> detalles = new ArrayList<>();

    /** Establece la fecha de generación antes de persistir */
    @PrePersist
    protected void onCreate() {
        fechaGeneracion = LocalDateTime.now();
    }

    /**
     * Agrega un detalle a la rutina
     * 
     * @param detalle Detalle a agregar
     */
    public void addDetalle(DetalleRutina detalle) {
        detalles.add(detalle);
        detalle.setRutinaIa(this);
    }

    /**
     * Elimina un detalle de la rutina
     * 
     * @param detalle Detalle a eliminar
     */
    public void removeDetalle(DetalleRutina detalle) {
        detalles.remove(detalle);
        detalle.setRutinaIa(null);
    }
}