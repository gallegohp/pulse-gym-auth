package com.pulse_gym.ms_operation.enums;

public enum EnumTipoMantenimiento {

    PREVENTIVO(1L),
    CORRECTIVO(2L);

    private final Long id;

    EnumTipoMantenimiento(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static EnumTipoMantenimiento fromId(Long id) {

        for (EnumTipoMantenimiento tipo : values()) {

            if (tipo.getId().equals(id)) {
                return tipo;
            }
        }

        throw new IllegalArgumentException("Tipo de mantenimiento no válido: " + id);
    }
}