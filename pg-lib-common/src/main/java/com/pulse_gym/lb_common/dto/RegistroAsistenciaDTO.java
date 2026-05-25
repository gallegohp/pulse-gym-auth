package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegistroAsistenciaDTO {

    /**
     * ID del usuario que se registra la asistencia
     */
    @NotNull(message = "El ID de usuario es obligatorio")
    private Long idUsuario;
    
    /**
     * ID de la sede a la que se registra la asistencia
     */
    @NotNull(message = "El ID de sede es obligatorio")
    private Long idSede;
    
    /**
     * Tipo de acceso (WEB o APP)
     */
    @NotBlank(message = "El tipo de acceso es obligatorio (WEB o APP)")
    private String tipoAcceso;
    
    /**
     * Identificador del dispositivo (opcional)
     */
    private String dispositivoId; // Opcional: identificador del dispositivo
}