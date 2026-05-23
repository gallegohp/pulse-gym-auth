package com.pulse_gym.lb_common.entity.auth;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumRol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "usuarios_auth")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 20)
    private EnumRol rol;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

}
