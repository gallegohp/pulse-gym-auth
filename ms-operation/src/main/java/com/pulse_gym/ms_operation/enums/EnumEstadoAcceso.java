package com.pulse_gym.ms_operation.enums;

public enum EnumEstadoAcceso {

    PERMITIDO(1L),
    DENEGADO(2L);

    private final Long id;

    EnumEstadoAcceso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static EnumEstadoAcceso fromId(Long id) {

        for (EnumEstadoAcceso estado : values()) {

            if (estado.getId().equals(id)) {
                return estado;
            }
        }

        throw new IllegalArgumentException("Estado de acceso no válido: " + id);
    }
}