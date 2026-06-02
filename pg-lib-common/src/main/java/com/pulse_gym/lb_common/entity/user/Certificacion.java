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
@Data
@Table(name = "certificacion")
public class Certificacion {

    /** Id unico de certificacion */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_certificacion")
    private Long idCertificacion;

    /** Entrenador asociado a la certificación */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_usuario", nullable = false)
    private UsuarioPerfil entrenador;

    /** Nombre de la certificación */
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    /** URL del PDF de la certificación */
    @Column(name = "url_pdf", nullable = false, length = 500)
    private String urlPdf;

    /** Fecha de subida de la certificación */
    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    /** Descripción opcional de la certificación */
    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
    }
}
