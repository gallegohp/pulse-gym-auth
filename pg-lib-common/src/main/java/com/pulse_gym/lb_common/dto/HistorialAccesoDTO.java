package com.pulse_gym.lb_common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistorialAccesoDTO {

    /** ID del usuario */
    private Long usuarioId;

    /** Nombre completo del usuario */
    private String nombreUsuario;

    /** Fecha y hora del acceso */
    private LocalDateTime fechaHora;

    /** Tipo de acceso (WEB, APP, BIOMETRICO) */
    private String tipoAcceso;

    /** Resultado del acceso (EXITOSO, FALLIDO, BLOQUEADO) */
    private String resultado;

    /** Motivo en caso de fallo o bloqueo */
    private String motivo;

    /** ID de la sede */
    private Long sedeId;

    /** Nombre de la sede */
    private String nombreSede;
}