package com.pulse_gym.lb_common.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstadoMembresiaResponseDTO {

    /** ID del socio */
    private Long idSocio;

    /** Nombre del socio */
    private String nombreSocio;

    /** Email del socio */
    private String emailSocio;

    /** ID de la relación socio-membresía */
    private Long idSocioMembresia;

    /** ID de la membresía */
    private Long idMembresia;

    /** Nombre de la membresía */
    private String nombreMembresia;

    /** Fecha de inicio de la membresía */
    private LocalDate fechaInicio;

    /** Fecha de vencimiento de la membresía */
    private LocalDate fechaVencimiento;

    /** Estado de la membresía (ACTIVA, VENCIDA, CANCELADA, SUSPENDIDA, RENOVADA) */
    private String estado;

    /** Indica si la membresía está activa */
    private boolean activa;

    /** Indica si la membresía está vencida */
    private boolean vencida;

    /** Días restantes antes del vencimiento */
    private Long diasRestantes;

    /** Mensaje descriptivo del estado */
    private String mensaje;
}