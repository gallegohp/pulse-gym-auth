package com.pulse_gym.lb_common.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pulse_gym.lb_common.enums.EnumEstado;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Long idEquipo;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    @JsonIgnore
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    @JsonIgnore
    private Sede sede;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "marca", length = 50)
    private String marca;

    @Column(name = "modelo", length = 50)
    private String modelo;

    @Column(name = "numero_serie", unique = true, length = 100)
    private String numeroSerie;

    @Column(name = "fecha_adquisicion")
    private LocalDate fechaAdquisicion;

    @Column(name = "fecha_garantia")
    private LocalDate fechaGarantia;

    @Column(name = "ubicacion", length = 100)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EnumEstado estado;
}