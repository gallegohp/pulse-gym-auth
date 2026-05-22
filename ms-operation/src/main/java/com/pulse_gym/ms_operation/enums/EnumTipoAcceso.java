package com.pulse_gym.ms_operation.enums;

public enum EnumTipoAcceso {

    HUELLA(1L),
    WEB(2L),
    APP(3L);

    private final Long id;

    EnumTipoAcceso(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static EnumTipoAcceso fromId(Long id) {

        for (EnumTipoAcceso tipo : values()) {

            if (tipo.getId().equals(id)) {
                return tipo;
            }
        }

        throw new IllegalArgumentException("Tipo de acceso no válido: " + id);
    }
}