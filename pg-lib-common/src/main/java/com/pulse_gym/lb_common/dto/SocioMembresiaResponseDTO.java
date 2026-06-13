package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SocioMembresiaResponseDTO {

    /** ID de la membresía del socio */
    private Long idSocioMembresia;

    /** ID del socio */
    private Long idSocio;

    /** Nombre del socio */
    private String nombreSocio;

    /** Email del socio */
    private String emailSocio;

    /** ID de la membresía */
    private Long idMembresia;

    /** Nombre de la membresía */
    private String nombreMembresia;

    /** Precio total de la membresía */
    private BigDecimal precioTotal;

    /** Cantidad de la membresía */
    private Integer cantidad;

    /** Tipo de duración */
    private String tipoDuracion;

    /** Descripción de la duración */
    private String duracionDescripcion;

    /** Indica si la membresía incluye inteligencia artificial */
    private Boolean incluyeIA;

    /** Fecha de inicio de la membresía */
    private LocalDate fechaInicio;

    /** Fecha de vencimiento de la membresía */
    private LocalDate fechaVencimiento;

    /** Estado de la membresía */
    private String estado;

    /** Días restantes hasta el vencimiento */
    private Long diasRestantes;

    /** Indica si la membresía está activa */
    private Boolean estaActiva;

    /** Indica si la membresía está vencida */
    private Boolean estaVencida;

    /** Beneficios de la membresía */
    private String beneficios;

    /** Restricciones de la membresía */
    private String restricciones;

    /** Fecha de creación */
    private LocalDateTime fechaCreacion;

    /** Fecha de actualización */
    private LocalDateTime fechaActualizacion;
}
