package com.pulse_gym.lb_common.enums;

public enum EnumEventoAsociado {

    /**
     * Evento de notificacion para bienvenida
     */
    WELCOME,

    /**
     * Evento de verificacion al registrar nuevo usuario en auth
     */
    REGISTRO_USUARIO,
    
    /**
     * Evento de inicio de sesion de usuario
     */
    LOGIN_USUARIO,

    /**
     * Evento notificacion de recordatirio de pago
     */
    PAYMENT_REMINDER,

    /**
     * Evento de notificacion al conseguir un logro
     */
    ACHIEVEMENT,

    /**
     * Evento de notificacion para alerta de mantenimiento
     */
    MAINTENANCE_ALERT,

    /**
     * Evento de notificacion promocional
     */
    PROMOTION
}
