package com.pulse_gym.ms_notifications.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.notification.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Cuenta notificaciones enviadas por un usuario desde una fecha
     */
    long countById_usuarioAndFechaEnvioAfter(Long idUsuario, LocalDateTime fecha);

    /**
     * Cuenta notificaciones enviadas por un usuario en un rango de fechas
     */
    long countById_usuarioAndFechaEnvioBetween(Long idUsuario, LocalDateTime inicio, LocalDateTime fin);
}
