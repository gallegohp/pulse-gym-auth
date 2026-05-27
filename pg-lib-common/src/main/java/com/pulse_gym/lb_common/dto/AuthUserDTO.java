package com.pulse_gym.lb_common.dto;

import com.pulse_gym.lb_common.enums.EnumRol;

import lombok.Data;

@Data
public class AuthUserDTO {
    private Long id;
    private String email;
    private String username;
    private EnumRol rol;
    private Boolean estado;
}
