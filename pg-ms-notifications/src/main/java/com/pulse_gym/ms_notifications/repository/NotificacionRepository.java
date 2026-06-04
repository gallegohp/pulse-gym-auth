package com.pulse_gym.ms_notifications.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.notification.Notificacion;

import org.springframework.data.repository.query.Param;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Cuenta notificaciones efectivas (no rechazadas) enviadas en el ultimo minuto
     */
    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.id_usuario = :idUsuario AND n.fechaEnvio > :fecha AND n.estado <> 'RECHAZADO'")
    long countEfectivosUltimoMinuto(@Param("idUsuario") Long idUsuario, @Param("fecha") LocalDateTime fecha);

    /**
     * Cuenta notificaciones efectivas (no rechazadas) enviadas en el ultimo dia
     */
    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.id_usuario = :idUsuario AND n.fechaEnvio > :fecha AND n.estado <> 'RECHAZADO'")
    long countEfectivosUltimoDia(@Param("idUsuario") Long idUsuario, @Param("fecha") LocalDateTime fecha);
}
