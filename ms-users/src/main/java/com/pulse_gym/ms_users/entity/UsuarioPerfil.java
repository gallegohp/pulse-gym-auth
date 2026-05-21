package com.pulse_gym.ms_users.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pulse_gym.ms_users.enums.NivelExperiencia;
import com.pulse_gym.ms_users.enums.Rol;
import com.pulse_gym.ms_users.enums.Turno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario_perfil")
public class UsuarioPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, columnDefinition = "ENUM('administrador', 'entrenador', 'recepcionista', 'socio')")
    private Rol rol;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "documento_identidad", nullable = false, unique = true, length = 20)
    private String documentoIdentidad;

    @Column(name = "foto_url", nullable = false, length = 255)
    private String fotoUrl;

    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "especialidad", nullable = false, length = 100)
    private String especialidad;

    @Column(name = "años_experiencia", nullable = false)
    private Short anosExperiencia;

    @Column(name = "horario_disponibilidad", nullable = false, length = 255)
    private String horarioDisponibilidad;

    @Column(name = "tarifa_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false, columnDefinition = "ENUM('mañana', 'tarde', 'noche')")
    private Turno turno;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "contacto_emergencia_nombre", nullable = false, length = 100)
    private String contactoEmergenciaNombre;

    @Column(name = "contacto_emergencia_telefono", nullable = false, length = 20)
    private String contactoEmergenciaTelefono;

    @Column(name = "objetivo_principal", nullable = false, length = 255)
    private String objetivoPrincipal;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_experiencia", nullable = false, columnDefinition = "ENUM('novato', 'intermedio', 'avanzado')")
    private NivelExperiencia nivelExperiencia;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "id_sede")
    private Integer idSede;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }

}