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
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "perfil_medico")
public class PerfilMedico {

    /** Id unico de perfil medico */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil_medico")
    private Long idPerfilMedico;

    /** Usuario asociado al perfil médico */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_usuario", nullable = false, unique = true)
    private UsuarioPerfil socio;

    /** Peso en kilogramos */
    @Column(name = "peso_kg", precision = 10, scale = 2)
    private BigDecimal pesoKg;

    /** Estatura en centímetros */
    @Column(name = "estatura_cm")
    private Short estaturaCm;

    /** Alergias */
    @Column(name = "alergias", columnDefinition = "TEXT")
    private String alergias;

    /** Condiciones crónicas */
    @Column(name = "condiciones_cronicas", columnDefinition = "TEXT")
    private String condicionesCronicas;

    /** Lesiones previas */
    @Column(name = "lesiones_previas", columnDefinition = "TEXT")
    private String lesionesPrevias;

    /** Porcentaje de grasa */
    @Column(name = "porcentaje_grasa", precision = 5, scale = 2)
    private BigDecimal porcentajeGrasa;

    /** Fecha de actualización */
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    /** Acción a realizar antes de persistir o actualizar */
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}