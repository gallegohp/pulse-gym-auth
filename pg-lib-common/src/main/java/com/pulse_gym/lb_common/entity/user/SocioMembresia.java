package com.pulse_gym.lb_common.entity.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoSocioMembresia;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "socio_membresia")
@Data
public class SocioMembresia {

    /** Identificador único de la relación socio-membresía */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio_membresia")
    private Long idSocioMembresia;

    /** Socio al que se le asigna la membresía */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_socio", nullable = false)
    private UsuarioPerfil socio;

    /** Membresía asignada */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_membresia", nullable = false)
    private Membresia membresia;

    /** Fecha de inicio de la membresía */
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    /** Fecha de vencimiento de la membresía */
    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    /** Estado actual de la membresía */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EnumEstadoSocioMembresia estado = EnumEstadoSocioMembresia.ACTIVA;

    /** Fecha de creación del registro */
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /** Fecha de última actualización */
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /** Observaciones sobre la asignación/cancelación */
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    /** Método que se ejecuta antes de persistir el registro */
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    /** Método que se ejecuta antes de actualizar el registro */
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    /**
     * Verifica si la membresía está vencida
     * @return true si la fecha de vencimiento es anterior a hoy
     */
    public boolean isVencida() {
        return LocalDate.now().isAfter(fechaVencimiento);
    }

    /**
     * Verifica si la membresía está activa
     * @return true si está ACTIVA y no está vencida
     */
    public boolean isActiva() {
        return estado == EnumEstadoSocioMembresia.ACTIVA && !isVencida();
    }

    /**
     * Calcula los días restantes de la membresía
     * @return días restantes o 0 si está vencida
     */
    public long getDiasRestantes() {
        if (isVencida()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }
}