package com.pulse_gym.ms_operation.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.operation.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long>, 
                                        JpaSpecificationExecutor<Equipo> {
    
    Optional<Equipo> findByNumeroSerie(String numeroSerie);
    
} 
