package com.pulse_gym.lb_common.dto;

import com.pulse_gym.lb_common.enums.EnumPreferenciaUsuario;

import lombok.Data;

@Data
public class PreferenciaUsuarioResponseDTO {

    /**
     * Identificador del usuario en auth
     */
    private Long idUsuario;

    /**
     * Canal preferido de notificaciones
     */
    private EnumPreferenciaUsuario preferencia;

    /**
     * Indica si el usuario desea notificaciones de logros
     */
    private Boolean logrosHabilitado;

    /**
     * Indica si el usuario desea notificaciones de mantenimiento
     */
    private Boolean mantenimientosHabilitado;

    /**
     * Indica si el usuario desea notificaciones promocionales
     */
    private Boolean promocionesHabilitado;
}
