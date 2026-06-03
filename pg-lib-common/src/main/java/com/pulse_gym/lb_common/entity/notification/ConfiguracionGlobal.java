package com.pulse_gym.lb_common.entity.notification;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "configuracion_global")
@Data
public class ConfiguracionGlobal {
    
    /**
     * Identificador de la configuración global
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long idConfiguracion;

    /**
     * Cantidad máxima de notificaciones por día
     */
    @Column(name = "max_notificaciones_por_dia", nullable = false)
    private Long max_notificaciones_por_dia;

    /**
     * Cantidad máxima de notificaciones por minuto
     */
    @Column(name = "max_notificaciones_por_minuto", nullable = false)
    private Long max_notificaciones_por_minuto;

}
