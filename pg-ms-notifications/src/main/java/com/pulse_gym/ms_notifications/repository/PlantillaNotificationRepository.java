package com.pulse_gym.ms_notifications.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;

@Repository
public interface PlantillaNotificationRepository extends JpaRepository<PlantillaNotificacion, Long> {

}
