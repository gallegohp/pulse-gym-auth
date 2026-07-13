package com.pulse_gym.ms_users.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pulse_gym.lb_common.entity.user.Ejercicio;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long>, JpaSpecificationExecutor<Ejercicio> {

    /**
     * Busca todos los ejercicios activos
     * 
     * @return Lista de ejercicios activos
     */
    List<Ejercicio> findByActivoTrue();

    /**
     * Busca ejercicios activos por grupo muscular
     * 
     * @param grupoMuscular Grupo muscular a filtrar
     * @return Lista de ejercicios activos del grupo
     */
    List<Ejercicio> findByGrupoMuscularAndActivoTrue(String grupoMuscular);

    /**
     * Busca ejercicios activos por equipo necesario
     * 
     * @param equipoNecesario Equipo a filtrar
     * @return Lista de ejercicios activos que usan ese equipo
     */
    List<Ejercicio> findByEquipoNecesarioAndActivoTrue(String equipoNecesario);

    /**
     * Busca ejercicios activos con dificultad entre un rango
     * 
     * @param min Dificultad mínima
     * @param max Dificultad máxima
     * @return Lista de ejercicios dentro del rango
     */
    List<Ejercicio> findByDificultadBetweenAndActivoTrue(Integer min, Integer max);

    /**
     * Verifica si existe un ejercicio activo con el nombre indicado
     * 
     * @param nombre Nombre del ejercicio
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombreAndActivoTrue(String nombre);
}