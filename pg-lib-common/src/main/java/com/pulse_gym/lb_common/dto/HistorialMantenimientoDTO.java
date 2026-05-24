package com.pulse_gym.lb_common.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HistorialMantenimientoDTO {
    /**
     * Identificador único del mantenimiento, generado automáticamente por la base de datos
     */
    private Long idMantenimiento;
    /**
     * Fecha en la que se realizó el mantenimiento
     */
    private LocalDate fechaServicio;
    /**
     * Tipo de mantenimiento realizado (preventivo, correctivo, etc.)
     */
    private String tipo;
    /**
     * Descripción detallada del mantenimiento realizado
     */
    private String descripcion;
    /**
     * Costo del mantenimiento
     */
    private BigDecimal costo;
    /**
     * Técnico responsable del mantenimiento
     */
    private String tecnicoResponsable;
    /**
     * Nombre de la empresa proveedora del equipo o servicio de mantenimiento
     */
    private String proveedorNombre;  
    
    private LocalDate proximoMantenimiento;
}