package com.pulse_gym.lb_common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialAccesoFiltroDTO {

    /** ID del usuario (opcional, null = todos) */
    private Long usuarioId;

    /** Fecha de inicio del rango (opcional) */
    private LocalDateTime fechaInicio;

    /** Fecha de fin del rango (opcional) */
    private LocalDateTime fechaFin;

    /**
     * Tipo de acceso: "WEB" (incluye WEB y APP) o "HUELLA" (BIOMETRICO)
     * Si es null, se traen todos
     */
    private String tipoAcceso;

    /**
     * Resultado: "EXITOSO", "FALLIDO", "BLOQUEADO"
     * Si es null, se traen todos
     */
    private String resultado;
}