// ConsultaEquipoRequest.java
package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class ConsultaEquipoRequestDTO {
    // Búsqueda por nombre (parcial)
    private String nombre;
    
    // Búsqueda por marca (parcial)
    private String marca;
    
    // Búsqueda por estado (exacto)
    private String estado; 
    
    // Búsqueda por ubicación (parcial)
    private String ubicacion;
    
    // Búsqueda por sede (ID de la sede)
    private Long idSede;
    
    // Para búsquedas combinadas
    private Integer page; // página (opcional)
    private Integer size; // tamaño de página (opcional)
}