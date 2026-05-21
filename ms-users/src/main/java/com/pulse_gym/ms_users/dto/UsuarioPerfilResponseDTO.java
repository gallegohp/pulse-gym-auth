package com.pulse_gym.ms_users.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pulse_gym.ms_users.enums.NivelExperiencia;
import com.pulse_gym.ms_users.enums.Rol;
import com.pulse_gym.ms_users.enums.Turno;

import lombok.Data;

@Data
public class UsuarioPerfilResponseDTO {

    private Long idUsuario;
    private Rol rol;
    private String nombre;
    private String apellido;
    private String telefono;
    private String documentoIdentidad;
    private String fotoUrl;
    private LocalDate fechaContratacion;
    private String specialty;
    private String especialidad;
    private Short anosExperiencia;
    private String horarioDisponibilidad;
    private BigDecimal tarifaHora;
    private Turno turno;
    private LocalDate fechaNacimiento;
    private String contactoEmergenciaNombre;
    private String contactoEmergenciaTelefono;
    private String objetivoPrincipal;
    private NivelExperiencia nivelExperiencia;
    private LocalDateTime fechaRegistro;
    private Integer idSede;
}
