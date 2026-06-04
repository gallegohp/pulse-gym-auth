package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.pulse_gym.lb_common.enums.EnumNivelExperiencia;
import com.pulse_gym.lb_common.enums.EnumTurno;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompletarPerfilRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres.")
    private String apellido;

    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres.")
    private String telefono;

    @NotBlank(message = "El documento de identidad es obligatorio.")
    @Size(max = 20, message = "El documento no puede superar los 20 caracteres.")
    private String documentoIdentidad;

    @NotBlank(message = "La URL de la foto es obligatoria.")
    @Size(max = 255, message = "La URL de la foto no puede superar los 255 caracteres.")
    private String fotoUrl;

    @NotNull(message = "La fecha de contratación es obligatoria.")
    @PastOrPresent(message = "La fecha de contratación no puede ser del futuro.")
    private LocalDate fechaContratacion;

    @NotBlank(message = "La especialidad es obligatoria.")
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres.")
    private String especialidad;

    @NotNull(message = "Los años de experiencia son obligatorios.")
    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos.")
    private Short anosExperiencia;

    @NotBlank(message = "El horario de disponibilidad es obligatorio.")
    @Size(max = 255, message = "El horario no puede superar los 255 caracteres.")
    private String horarioDisponibilidad;

    @NotNull(message = "La tarifa por hora es obligatoria.")
    @DecimalMin(value = "0.0", inclusive = true, message = "La tarifa por hora no puede ser negativa.")
    private BigDecimal tarifaHora;

    @NotNull(message = "El turno es obligatorio.")
    private EnumTurno turno;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El nombre del contacto de emergencia es obligatorio.")
    @Size(max = 100, message = "El nombre del contacto no puede superar los 100 caracteres.")
    private String contactoEmergenciaNombre;

    @NotBlank(message = "El teléfono del contacto de emergencia es obligatorio.")
    @Size(max = 20, message = "El teléfono del contacto no puede superar los 20 caracteres.")
    private String contactoEmergenciaTelefono;

    @NotBlank(message = "El objetivo principal es obligatorio.")
    @Size(max = 255, message = "El objetivo no puede superar los 255 caracteres.")
    private String objetivoPrincipal;

    @NotNull(message = "El nivel de experiencia es obligatorio.")
    private EnumNivelExperiencia nivelExperiencia;

    @NotNull(message = "El ID de la sede es obligatorio.")
    private Integer idSede;
}
