package com.pulse_gym.lb_common.enums;

public enum EnumRol {

    administrador(1L),
    entrenador(2L),
    recepcionista(3L),
    socio(4L);

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

    /**
    * Método para obtener el rol a partir de su id
    * @param id
    * @return EnumRol
    */
    public static EnumRol fromId(Long id) {
        for (EnumRol rol : values()) {
            if (rol.getId().equals(id)) {
                return rol;
            }
    }

        throw new IllegalArgumentException("Rol no válido: " + id);
    }
}

