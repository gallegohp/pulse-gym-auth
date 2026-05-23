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
@Table(name = "sede")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sede {

    /**
    * Identificador único de la sede, generado automáticamente por la base de datos
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Long idSede;

    /**
     * Nombre de la sede del gimnasio (por ejemplo, "Sede Central", "Sede Norte", etc.)
     */
    @Column(name = "nombre_sede", nullable = false, length = 100)
    private String nombreSede;

    /**
     * Dirección física de la sede del gimnasio
     */
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    /**
     * Número de teléfono de contacto de la sede
     */
    @Column(name = "telefono", length = 20)
    private String telefono;

    /**
     * Ciudad donde se encuentra la sede del gimnasio
     */
    @Column(name = "ciudad", nullable = false, length = 100)
    private String ciudad;

    /**
     * Lista de equipos disponibles en la sede. Se ignora en la serialización JSON para evitar ciclos de referencia
     */
    @JsonIgnore
    @OneToMany(mappedBy = "sede")
    private List<Equipo> equipos;
}