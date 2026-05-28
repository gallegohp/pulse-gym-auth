package com.pulse_gym.lb_common.entity.notification;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoNotificacion;

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
import lombok.Data;

@Table(name = "notificacion")
@Entity
@Data
public class Notificacion {
    
    /**
     * Identificador de la notificacion
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Long id_notification;

    
    @ManyToOne
    @JoinColumn(name = "id_plantilla")
    private PlantillaNotificacion id_plantilla;

    @Column(name = "id_usuario", nullable = false)
    private Long id_usuario;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "contenido", nullable = false)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private EnumEstadoNotificacion estado;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;
}
