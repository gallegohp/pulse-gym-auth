package com.pulse_gym.lb_common.dto;

import com.pulse_gym.lb_common.enums.EnumPreferenciaUsuario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PreferenciaUsuarioRequestDTO {

    /**
     * Canal preferido de notificaciones
     */
    @NotNull(message = "La preferencia de canal es obligatoria")
    private EnumPreferenciaUsuario preferencia;

    /**
     * Indica si el usuario desea notificaciones de logros
     */
    @NotNull(message = "La preferencia de logros es obligatoria")
    private Boolean logrosHabilitado;

    /**
     * Indica si el usuario desea notificaciones de mantenimiento
     */
    @NotNull(message = "La preferencia de mantenimiento es obligatoria")
    private Boolean mantenimientosHabilitado;

    /**
     * Indica si el usuario desea notificaciones promocionales
     */
    @NotNull(message = "La preferencia de promociones es obligatoria")
    private Boolean promocionesHabilitado;
}
