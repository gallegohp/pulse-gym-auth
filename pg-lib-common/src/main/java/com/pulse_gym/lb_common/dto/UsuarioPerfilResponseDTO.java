package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoUsuario;
import com.pulse_gym.lb_common.enums.EnumRol;
import com.pulse_gym.lb_common.enums.EnumNivelExperiencia;
import com.pulse_gym.lb_common.enums.EnumTurno;

import lombok.Data;

@Data
public class UsuarioPerfilResponseDTO {

    /**
     * Identificador único del usuario asignado por el sistema
     */
    private Long idUsuario;

    /**
     * Rol asignado al usuario dentro del sistema de Pulse Gym
     */
    private EnumRol rol;

    /**
     * Estado actual del usuario, indicando si está activo, inactivo o suspendido
     */
    private EnumEstadoUsuario estado;

    /**
     * Correo electrónico registrado del usuario, utilizado para comunicaciones y
     * autenticación
     */

    private String email;
    /**
     * Nombres completos del usuario
     */
    private String nombre;

    /**
     * Apellidos completos del usuario
     */
    private String apellido;

    /**
     * Número de teléfono de contacto del usuario
     */
    private String telefono;

    /**
     * Número de documento de identidad único del usuario
     */
    private String documentoIdentidad;

    /**
     * URL de la imagen o foto de perfil del usuario
     */
    private String fotoUrl;

    /**
     * Fecha de contratación o vinculación del usuario
     */
    private LocalDate fechaContratacion;

    /**
     * Especialidad técnica o enfoque profesional del usuario
     */
    private String especialidad;

    /**
     * Años de experiencia laboral acumulados por el usuario
     */
    private Short anosExperiencia;

    /**
     * Horarios de disponibilidad o franjas horarias registradas
     */
    private String horarioDisponibilidad;

    /**
     * Tarifa o costo monetario asignado por cada hora de servicio
     */
    private BigDecimal tarifaHora;

    /**
     * Turno de trabajo o asistencia asignado en el sistema
     */
    private EnumTurno turno;

    /**
     * Fecha de nacimiento del usuario
     */
    private LocalDate fechaNacimiento;

    /**
     * Nombre de la persona registrada para contacto en caso de emergencia
     */
    private String contactoEmergenciaNombre;

    /**
     * Teléfono de la persona registrada para contacto en caso de emergencia
     */
    private String contactoEmergenciaTelefono;

    /**
     * Objetivo o meta principal que el usuario busca alcanzar en el gimnasio
     */
    private String objetivoPrincipal;

    /**
     * Nivel de experiencia física o deportiva actual del usuario
     */
    private EnumNivelExperiencia nivelExperiencia;

    /**
     * Fecha y hora exacta en la que el usuario fue registrado en la plataforma
     */
    private LocalDateTime fechaRegistro;

    /**
     * Identificador de la sede física a la cual se encuentra vinculado el usuario
     */
    private Integer idSede;

    /**
     * Id biometrico del usuario
     */
    private String biometricDeviceId;

}
