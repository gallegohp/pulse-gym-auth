package com.pulse_gym.ms_operation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.operation.Sede;
import java.util.List;
import java.util.Optional;

@Repository
public interface SedeRepository extends JpaRepository<Sede, Long> {
    
    Optional<Sede> findByNombreSede(String nombreSede);
    
    List<Sede> findByNombreSedeContainingIgnoreCase(String nombreSede);
    
    List<Sede> findByCiudadContainingIgnoreCase(String ciudad);
    
    boolean existsByNombreSede(String nombreSede);
    
    Page<Sede> findAll(Pageable pageable);
    
    @Query("SELECT COUNT(e) FROM Equipo e WHERE e.sede.idSede = :idSede")
    Long countEquiposBySedeId(@Param("idSede") Long idSede);
}