package com.pulse_gym.lb_common.entity.operation;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pulse_gym.lb_common.enums.EnumEstado;
import com.pulse_gym.lb_common.enums.EnumEstadoReporte;
import com.pulse_gym.lb_common.enums.EnumUrgencia;

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
@Table(name = "equipo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {

    /**
    * Identificador único del equipo, generado automáticamente por la base de datos
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Long idEquipo;

    /**
     * Proveedor del equipo
     */
    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    @JsonIgnore
    private Proveedor proveedor;

    /**
     * Sede a la que pertenece el equipo
     */
    @ManyToOne
    @JoinColumn(name = "id_sede")
    @JsonIgnore
    private Sede sede;

    /**
     * Nombre del equipo
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Descripción del equipo
     */
    @Column(name = "marca", length = 50)
    private String marca;

    /**
     * Modelo del equipo
     */
    @Column(name = "modelo", length = 50)
    private String modelo;

    /**
     * Número de serie del equipo, único para cada equipo registrado
     */
    @Column(name = "numero_serie", unique = true, length = 100)
    private String numeroSerie;

    /**
     * Fecha de adquisición del equipo
     */
    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;

    /**
     * Fecha de garantía del equipo
     */
    @Column(name = "fecha_garantia")
    private LocalDate fechaGarantia;

    /**
     * Ubicación del equipo dentro de la sede (por ejemplo, "Sala de pesas", "Área de cardio", etc.)
     */
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    /**
    * Estado del equipo (activo, en mantenimiento, fuera de servicio, etc.)
    */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EnumEstado estado;
    
    /**    
     * Urgencia de la falla reportada
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "urgencia_falla")
    private EnumUrgencia urgenciaFalla = EnumUrgencia.NINGUNA;

    /**
     * Descripción de la falla
     */
    @Column(name = "descripcion_falla", columnDefinition = "TEXT")
    private String descripcionFalla;

    /**
     * Estado del reporte de falla
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reporte")
    private EnumEstadoReporte estadoReporte;
}