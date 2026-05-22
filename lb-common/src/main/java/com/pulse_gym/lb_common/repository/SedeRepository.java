package com.pulse_gym.lb_common.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.Sede;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {
    
} 
