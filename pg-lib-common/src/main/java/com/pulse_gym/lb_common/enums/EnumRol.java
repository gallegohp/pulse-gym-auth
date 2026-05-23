package com.pulse_gym.lb_common.enums;

public enum EnumRol {
    /**
     * Rol de administrador, con permisos para gestionar el sistema y acceder a todas las funcionalidades
     */
    administrador,

    /**
     * Rol de entrenador, con permisos para gestionar las rutinas de entrenamiento, asignar ejercicios a los socios y llevar un seguimiento de su progreso
     */
    entrenador,

    /**
     * Rol de recepcionista, con permisos para gestionar las reservas de los socios, registrar su asistencia y brindar información sobre los servicios del gimnasio
     */
    recepcionista,

    /**
     * Rol de socio, con permisos para acceder a su perfil, reservar clases, consultar su historial de entrenamiento y recibir recomendaciones personalizadas
     */
    socio
}