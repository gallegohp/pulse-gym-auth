package com.pulse_gym.ms_notifications.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.notification.PlantillaDisenoEmail;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

/**
 * Repositorio para gestionar las plantillas de diseño de emails
 */
@Repository
public interface PlantillaDisenoEmailRepository extends JpaRepository<PlantillaDisenoEmail, Long> {

    /**
     * Busca un diseño activo por su nombre
     * @param nombre Nombre del diseño
     * @return Diseño encontrado o vacío
     */
    Optional<PlantillaDisenoEmail> findByNombreAndEliminadoFalse(String nombre);

    /**
     * Busca un diseño activo por evento asociado
     * @param evento Evento asociado
     * @return Diseño encontrado o vacío
     */
    Optional<PlantillaDisenoEmail> findByEventoAsociadoAndEliminadoFalse(EnumEventoAsociado evento);

    /**
     * Busca el diseño por defecto (cuando no hay evento específico)
     * @return Diseño por defecto
     */
    Optional<PlantillaDisenoEmail> findByNombreAndEliminadoFalseAndActivoTrue(String nombre);
}