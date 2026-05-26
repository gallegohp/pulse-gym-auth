package com.pulse_gym.lb_common.enums;

public enum EnumEstado {

    /**
     * el equipo esta en condiciones óptimas de uso, sin problemas reportados y listo para ser utilizado en cualquier momento
    */
    OPERATIVO,

    /**
     * el equipo presenta algún tipo de falla o daño que afecta su funcionamiento normal, pero aún puede ser utilizado con precaución o para tareas específicas mientras se programa su reparación
     */
    MANTENIMIENTO,

    /**
     * el equipo no puede ser utilizado debido a una falla grave, daño irreparable o porque se encuentra en proceso de reparación, y no está disponible para su uso hasta que se resuelva la situación
     */
    FUERA_DE_SERVICIO,
    
    /**
     * el equipo ha sido retirado de la operación activa del gimnasio, ya sea por obsolescencia, daño irreparable o porque se ha decidido no repararlo, y no está disponible para su uso ni para futuras reparaciones
     */
    RETIRADO;

    
}