package com.pulse_gym.ms_notifications.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulse_gym.lb_common.entity.notification.PreferenciaUsuario;

public interface PreferenciaUsuarioRepository extends JpaRepository<PreferenciaUsuario, Long> {

    /**
     * Busca las preferencias de un usuario por su identificador en auth
     */
    Optional<PreferenciaUsuario> findByIdUsuario(Long idUsuario);
}
