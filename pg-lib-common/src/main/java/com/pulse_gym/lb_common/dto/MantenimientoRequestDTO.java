// MantenimientoRequestDTO.java
package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MantenimientoRequestDTO {
    
    @NotNull(message = "El ID del equipo es obligatorio")
    private Long idEquipo;
    
    private Long idProveedor; // Opcional, puede ser null para mantenimiento interno
    
    @NotNull(message = "La fecha de servicio es obligatoria")
    @PastOrPresent(message = "La fecha de servicio no puede ser futura")
    private LocalDate fechaServicio;
    
    @NotNull(message = "El tipo de mantenimiento es obligatorio")
    private String tipo; // PREVENTIVO, CORRECTIVO
    
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;
    
    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El costo debe ser mayor a 0")
    private BigDecimal costo;
    
    @Size(max = 100, message = "El nombre del técnico no puede exceder los 100 caracteres")
    private String tecnicoResponsable;
    
    @Future(message = "La fecha del próximo mantenimiento debe ser futura")
    private LocalDate proximoMantenimiento;
}