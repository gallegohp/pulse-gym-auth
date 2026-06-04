package com.pulse_gym.ms_notifications.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.notification.Notificacion;

import feign.Param;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.id_usuario = :idUsuario AND n.fechaEnvio > :fecha")
    long countById_usuarioAndFechaEnvioAfter(@Param("idUsuario") Long idUsuario, @Param("fecha") LocalDateTime fecha);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.id_usuario = :idUsuario AND n.fechaEnvio BETWEEN :inicio AND :fin")
    long countById_usuarioAndFechaEnvioBetween(@Param("idUsuario") Long idUsuario,
            @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(n) FROM Notificacion n WHERE n.id_usuario = :idUsuario AND n.fechaEnvio > :fecha AND n.estado <> 'RECHAZADO'")
    long countEfectivosUltimoMinuto(@Param("idUsuario") Long idUsuario, @Param("fecha") LocalDateTime fecha);
}
