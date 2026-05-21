package com.pulse_gym.ms_auth.enums;

public enum EnumRol {

    ADMIN(1L),
    CLIENTE(2L);

    private final Long id;

    /**
     * Constructor del enum RolEnum
     * @param id
     */
    EnumRol(Long id) {
        this.id = id;
    }
    /**
     * Método para obtener el id del rol
     * @return
     */
    public Long getId() {
        return id;
    }
}

