package com.pulse_gym.ms_notifications.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

@Repository
public interface PlantillaNotificationRepository extends JpaRepository<PlantillaNotificacion, Long> {

    /**
     * Obtiene plantillas activas no eliminadas
     */
    List<PlantillaNotificacion> findByEliminadaFalse();

    /**
     * Busca plantillas activas asociadas a un evento
     */
    List<PlantillaNotificacion> findByEventosAsociadosContainingAndEstadoTrueAndEliminadaFalse(
            EnumEventoAsociado evento);
}
