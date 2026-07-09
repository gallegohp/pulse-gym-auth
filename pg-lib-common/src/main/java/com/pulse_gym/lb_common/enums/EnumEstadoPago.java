package com.pulse_gym.lb_common.enums;

public enum EnumEstadoPago {

    /** Pago iniciado pero aún no completado */
    PENDIENTE,

    /** Pago aprobado y registrado exitosamente */
    APROBADO,

    /** Pago rechazado por el sistema de pagos o por la pasarela */
    RECHAZADO,

    /** Pago cancelado por el usuario */
    ANULADO
}
