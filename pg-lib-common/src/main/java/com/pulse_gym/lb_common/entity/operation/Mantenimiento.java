package com.pulse_gym.lb_common.entity.operation;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pulse_gym.lb_common.enums.EnumTipoMantenimiento;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mantenimiento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mantenimiento {

    /**
     * Identificador único del mantenimiento, generado automáticamente por la base de datos
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Long idMantenimiento;

    /**
     * Equipo al que se le realiza el mantenimiento
     */
    @ManyToOne
    @JoinColumn(name = "id_equipo", nullable = false)
    private Equipo equipo;

    /**
     * Proveedor que realiza el mantenimiento (puede ser null si es un mantenimiento interno)
     */
    @ManyToOne
    @JoinColumn(name = "id_proveedor", nullable = true)
    private Proveedor proveedor;

    /**
     * Fecha en la que se realizó el mantenimiento
     */
    @Column(name = "fecha_servicio", nullable = false)
    private LocalDate fechaServicio;

    /**
     * Tipo de mantenimiento realizado (preventivo, correctivo)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumTipoMantenimiento tipo;

    /**
     * Descripción detallada del mantenimiento realizado
     */
    @Column(name = "descripcion", nullable = true ,columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Costo del mantenimiento
     */
    @Column(name = "costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal costo;

    /**
     * Técnico responsable del mantenimiento
     */
    @Column(name = "tecnico_responsable",nullable = true ,length = 100)
    private String tecnicoResponsable;

    /**
     * Fecha estimada del próximo mantenimiento (puede ser null si no se ha programado)
     */
    @Column(name = "proximo_mantenimiento", nullable = true)
    private LocalDate proximoMantenimiento;
}