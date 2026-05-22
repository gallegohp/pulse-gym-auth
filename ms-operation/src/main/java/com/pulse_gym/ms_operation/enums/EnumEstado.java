package com.pulse_gym.ms_operation.enums;

public enum EnumEstado {

    OPERATIVO(1L),
    MANTENIMIENTO(2L),
    FUERA_DE_SERVICIO(3L),
    RETIRADO(4L);

    private final Long id;

    EnumEstado(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static EnumEstado fromId(Long id) {

        for (EnumEstado estado : values()) {

            if (estado.getId().equals(id)) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado no válido: " + id);
    }
}