// ProveedorRequestDTO.java
package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProveedorRequestDTO {
    
    /**
     * Nombre de la empresa
     */
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 100, message = "El nombre de la empresa no puede exceder 100 caracteres")
    private String nombreEmpresa;
    
    /**
     * Nombre de contacto
     */
    @Size(max = 100, message = "El nombre de contacto no puede exceder 100 caracteres")
    private String contactoNombre;
    
    /**
     * Teléfono
     */
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "El teléfono debe contener solo números y caracteres permitidos (+ - ( ) espacio)")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    /**
     * Correo electrónico
     */
    @Email(message = "Debe ser un correo electrónico válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;
}