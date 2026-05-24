// MantenimientoRepository.java
package com.pulse_gym.ms_operation.repository;

import com.pulse_gym.lb_common.entity.operation.Mantenimiento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long>, 
                                                JpaSpecificationExecutor<Mantenimiento> {
    
    List<Mantenimiento> findByEquipoIdEquipo(Long idEquipo);
    
    List<Mantenimiento> findByEquipoIdEquipoOrderByFechaServicioDesc(Long idEquipo);
    
    List<Mantenimiento> findTop5ByEquipoIdEquipoOrderByFechaServicioDesc(Long idEquipo);
    
    Long countByEquipoIdEquipo(Long idEquipo);
}