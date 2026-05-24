// MantenimientoRepository.java
package com.pulse_gym.ms_operation.repository;

import com.pulse_gym.lb_common.entity.operation.Mantenimiento;
import com.pulse_gym.lb_common.enums.EnumTipoMantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long>, 
                                                JpaSpecificationExecutor<Mantenimiento> {
    
    // Buscar mantenimientos por equipo
    List<Mantenimiento> findByEquipoIdEquipo(Long idEquipo);
    
    // Buscar mantenimientos por rango de fechas
    List<Mantenimiento> findByFechaServicioBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Buscar mantenimientos por tipo
    List<Mantenimiento> findByTipo(EnumTipoMantenimiento tipo);
    
    // Buscar mantenimientos próximos
    @Query("SELECT m FROM Mantenimiento m WHERE m.proximoMantenimiento <= :fechaLimite AND m.proximoMantenimiento IS NOT NULL")
    List<Mantenimiento> findMantenimientosProximos(@Param("fechaLimite") LocalDate fechaLimite);
    
    // Obtener último mantenimiento de un equipo
    @Query("SELECT m FROM Mantenimiento m WHERE m.equipo.idEquipo = :idEquipo ORDER BY m.fechaServicio DESC")
    List<Mantenimiento> findLastMantenimientoByEquipo(@Param("idEquipo") Long idEquipo, org.springframework.data.domain.Pageable pageable);
}