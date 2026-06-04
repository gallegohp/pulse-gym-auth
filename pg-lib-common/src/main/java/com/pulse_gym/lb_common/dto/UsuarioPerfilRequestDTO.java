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
public class UsuarioPerfilRequestDTO {

    /**
     * Nombre(s) del usuario. No puede estar vacío ni superar los 100 caracteres.
     */
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    private String nombre;

    /**
     * Apellido(s) del usuario. No puede estar vacío ni superar los 100 caracteres.
     */
    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres.")
    private String apellido;

    /** El correo del usuario */
    @NotBlank(message = "El Correo no puede estar vacío.")
    private String email;

    /**
     * Número de teléfono de contacto. Opcional, pero si se envía no puede superar
     * los 20 caracteres.
     */
    @Size(max = 20, message = "El teléfono no puede superar los 20 caracteres.")
    private String telefono;

    /**
     * Documento de identidad único del usuario. Es obligatorio y no puede superar
     * los 20 caracteres.
     */
    @NotBlank(message = "El documento de identidad es obligatorio.")
    @Size(max = 20, message = "El documento no puede superar los 20 caracteres.")
    private String documentoIdentidad;

    /**
     * URL o enlace a la foto de perfil del usuario. Es obligatoria y tiene un
     * límite de 255 caracteres.
     */
    @NotBlank(message = "La URL de la foto es obligatoria.")
    @Size(max = 255, message = "La URL de la foto no puede superar los 255 caracteres.")
    private String fotoUrl;

    /**
     * Fecha en la que ingresa o ingresó a trabajar en el gimnasio. Es obligatoria y
     * no puede ser futura.
     */
    @NotNull(message = "La fecha de contratación es obligatoria.")
    @PastOrPresent(message = "La fecha de contratación no puede ser del futuro.")
    private LocalDate fechaContratacion;

    /**
     * Área de enfoque profesional o especialidad del usuario. Es obligatoria y
     * máximo de 100 caracteres.
     */
    @NotBlank(message = "La especialidad es obligatoria.")
    @Size(max = 100, message = "La especialidad no puede superar los 100 caracteres.")
    private String especialidad;

    /**
     * Años de trayectoria del usuario en su especialidad. Es obligatorio y el valor
     * mínimo permitido es 0.
     */
    @NotNull(message = "Los años de experiencia son obligatorios.")
    @Min(value = 0, message = "Los años de experiencia no pueden ser negativos.")
    private Short anosExperiencia;

    /**
     * Días y horas en los que el usuario tiene disponibilidad. Es obligatorio con
     * un tope de 255 caracteres.
     */
    @NotBlank(message = "El horario de disponibilidad es obligatorio.")
    @Size(max = 255, message = "El horario no puede superar los 255 caracteres.")
    private String horarioDisponibilidad;

    /**
     * Costo o cobro monetario por cada hora laborada. Es obligatoria y no se
     * permiten valores negativos.
     */
    @NotNull(message = "La tarifa por hora es obligatoria.")
    @DecimalMin(value = "0.0", inclusive = true, message = "La tarifa por hora no puede ser negativa.")
    private BigDecimal tarifaHora;

    /**
     * Turno o jornada de trabajo/asistencia asignada en el sistema. Es obligatorio.
     */
    @NotNull(message = "El turno es obligatorio.")
    private EnumTurno turno;

    /**
     * Fecha de nacimiento del usuario. Es obligatoria y debe corresponder
     * estrictamente al pasado.
     */
    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada.")
    private LocalDate fechaNacimiento;

    /**
     * Nombre completo del contacto registrado para casos de emergencia.
     * Obligatorio, máximo 100 caracteres.
     */
    @NotBlank(message = "El nombre del contacto de emergencia es obligatorio.")
    @Size(max = 100, message = "El nombre del contacto no puede superar los 100 caracteres.")
    private String contactoEmergenciaNombre;

    /**
     * Número de teléfono de la persona de contacto para emergencias. Obligatorio,
     * máximo 20 caracteres.
     */
    @NotBlank(message = "El teléfono del contacto de emergencia es obligatorio.")
    @Size(max = 20, message = "El teléfono del contacto no puede superar los 20 caracteres.")
    private String contactoEmergenciaTelefono;

    /**
     * Objetivo principal o meta física que se busca gestionar en el perfil.
     * Obligatorio, máximo 255 caracteres.
     */
    @NotBlank(message = "El objetivo principal es obligatorio.")
    @Size(max = 255, message = "El objetivo no puede superar los 255 caracteres.")
    private String objetivoPrincipal;

    /**
     * Nivel de experiencia física o deportiva actual del usuario. Es obligatorio.
     */
    @NotNull(message = "El nivel de experiencia es obligatorio.")
    private EnumNivelExperiencia nivelExperiencia;

    /**
     * Identificador único de la sede física a la que se vincula el usuario. Es
     * obligatorio.
     */
    @NotNull(message = "El ID de la sede es obligatorio.")
    private Integer idSede;
}