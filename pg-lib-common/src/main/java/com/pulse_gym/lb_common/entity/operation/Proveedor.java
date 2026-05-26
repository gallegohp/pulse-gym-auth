package com.pulse_gym.lb_common.entity.operation;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "proveedor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    /**
    * Identificador único del proveedor, generado automáticamente por la base de datos
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Long idProveedor;

    /**
     * Nombre de la empresa proveedora del equipo o servicio de mantenimiento
     */
    @Column(name = "nombre_empresa", nullable = false, length = 100)
    private String nombreEmpresa;

    /**
     * Nombre de contacto principal en la empresa proveedora (puede ser null si no se tiene un contacto específico)
     */
    @Column(name = "contacto_nombre", length = 100)
    private String contactoNombre;

    /**
     * Número de teléfono de contacto del proveedor
     */
    @Column(name = "telefono", length = 20)
    private String telefono;

    /**
     * Correo electrónico de contacto del proveedor
     */
    @Column(name = "email", length = 150)
    private String email;

    /**
     * Lista de equipos suministrados por el proveedor. Se ignora en la serialización JSON para evitar ciclos de referencia
     */
    @JsonIgnore
    @OneToMany(mappedBy = "proveedor")
    private List<Equipo> equipos;
}