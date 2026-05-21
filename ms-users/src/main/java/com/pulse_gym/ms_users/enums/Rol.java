package com.pulse_gym.ms_users.enums;

public enum Rol {
    /**
     * Usuario con acceso total a la configuración, gestión de sedes, reportes
     * financieros y personal
     */
    administrador,

    /**
     * Personal encargado de la creación de rutinas, valoración física y asistencia
     * técnica de los socios
     */
    entrenador,

    /**
     * Personal encargado del control de accesos, atención al cliente, registro de
     * pagos y matrículas
     */
    recepcionista,

    /**
     * Cliente final que asiste a las instalaciones para hacer uso de las máquinas y
     * servicios contratados
     */
    socio
}
