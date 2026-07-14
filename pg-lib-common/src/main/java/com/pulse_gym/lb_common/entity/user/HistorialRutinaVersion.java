package com.pulse_gym.lb_common.entity.user;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "historial_rutina_version")
@Data
public class HistorialRutinaVersion {

    /** Identificador único del historial */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    /** Rutina a la que pertenece este historial */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_rutina_ia", nullable = false)
    private RutinaIA rutinaIa;

    /** Versión de la rutina en este historial */
    @Column(name = "version", nullable = false)
    private Integer version;

    /** Datos de la rutina en formato JSON */
    @Column(name = "datos_json", nullable = false, columnDefinition = "TEXT")
    private String datosJson;

    /** Usuario que realizó la modificación */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_modificado_por")
    private UsuarioPerfil modificadoPor;

    /** Fecha de la modificación */
    @Column(name = "fecha_modificacion", nullable = false)
    private LocalDateTime fechaModificacion;

    /** Motivo de la modificación */
    @Column(name = "motivo", length = 255)
    private String motivo;

    /** Establece la fecha de modificación antes de persistir */
    @PrePersist
    protected void onCreate() {
        fechaModificacion = LocalDateTime.now();
    }
}