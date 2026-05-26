package com.pulse_gym.lb_common.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AsistenciaResponseDTO {
    /**
     * ID de la asistencia
     */
    private Long idAsistencia;
    /**
     * ID del usuario que se registra la asistencia
     */
    private Long idUsuario;
    /**
     * Nombre de la sede
     */
    private String nombreSede;
    /**
     * Fecha y hora de entrada del usuario al gimnasio
     */
    private LocalDateTime fechaHoraEntrada;
    /**
     * Tipo de acceso (WEB o APP)
     */
    private String tipoAcceso;
    /**
     * Estado del acceso (permitido, denegado, etc.)
     */
    private String estadoAcceso;
    /**
     * Motivo de denegación del acceso (si aplica)
     */
    private String motivoDenegacion;
}