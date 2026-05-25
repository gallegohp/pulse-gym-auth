package com.pulse_gym.ms_operation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pulse_gym.lb_common.entity.operation.Proveedor;
import java.util.List;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long>, 
                                             JpaSpecificationExecutor<Proveedor> {
    
    // Buscar por nombre de empresa (parcial, case insensitive)
    List<Proveedor> findByNombreEmpresaContainingIgnoreCase(String nombreEmpresa);
    
    // Buscar por email
    List<Proveedor> findByEmail(String email);
    
    // Buscar por teléfono
    List<Proveedor> findByTelefono(String telefono);
    
    // Verificar si existe por email
    boolean existsByEmail(String email);
    
}