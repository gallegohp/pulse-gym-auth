package com.pulse_gym.lb_common.entity.notification;


import com.pulse_gym.lb_common.enums.EnumPreferenciaUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "preferencia_usuario")
@Data
public class PreferenciaUsuario {
    
    /**
     * Identificador de la preferencia de usuario
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idPreferencia;

    /**
     * Usuario que tiene la preferencia
     */
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    /**
     * Preferencia de notificaciones
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "preferencia", nullable = false)
    private EnumPreferenciaUsuario preferencia;

    /**
     * Preferencia de SI / NO tener notificaciones de logros
     */
    @Column(name = "logros_habilitado", nullable = false)
    private Boolean logros_habilitado;

    /**
     * Preferencia de SI / NO tener notificaciones de mantenimientos
     */
    @Column(name = "mantenimientos_habilitado", nullable = false)
    private Boolean mantenimientos_habilitado;

    /**
     * Preferencia de SI / NO tener notificaciones de promociones
     */
    @Column(name = "promociones_habilitado", nullable = false)
    private Boolean promociones_habilitado;
}
