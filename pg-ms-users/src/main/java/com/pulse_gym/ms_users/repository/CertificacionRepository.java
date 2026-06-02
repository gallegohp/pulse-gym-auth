package com.pulse_gym.ms_users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pulse_gym.lb_common.entity.user.Certificacion;

public interface CertificacionRepository extends JpaRepository<Certificacion, Long> {

    List<Certificacion> findByEntrenador_IdUsuario(Long idEntrenador);

    Optional<Certificacion> findByIdCertificacionAndEntrenador_IdUsuario(Long idCertificacion, Long idEntrenador);
}