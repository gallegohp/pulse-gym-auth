package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SedeUpdateDTO {
    
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombreSede;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    private String ciudad;
}