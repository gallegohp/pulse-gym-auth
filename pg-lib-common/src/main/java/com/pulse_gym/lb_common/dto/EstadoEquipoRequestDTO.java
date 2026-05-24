// EstadoRequestDTO.java
package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoEquipoRequestDTO {
    
    /**
     * Dto para manejar el cambio de estado de los equipos
     */
    @NotBlank(message = "El estado no puede estar vacío")
    @Pattern(regexp = "^(OPERATIVO|MANTENIMIENTO|FUERA_DE_SERVICIO|RETIRADO)$", 
            message = "Estado no válido. Valores permitidos: OPERATIVO, MANTENIMIENTO, FUERA_DE_SERVICIO, RETIRADO")
    private String estado;
}
