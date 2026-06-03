package com.pulse_gym.ms_notifications.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulse_gym.lb_common.entity.notification.ConfiguracionGlobal;

public interface ConfiguracionGlobalRespository extends JpaRepository<ConfiguracionGlobal, Long> {

    /**
     * Obtiene la configuracion global activa del sistema
     */
    Optional<ConfiguracionGlobal> findFirstByOrderByIdConfiguracionAsc();
}
