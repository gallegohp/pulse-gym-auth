package com.pulse_gym.lb_common.entity.operation;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoAcceso;
import com.pulse_gym.lb_common.enums.EnumTipoAcceso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asistencia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Asistencia {

    /**
     * Identificador único de la asistencia, generado automáticamente por la base de
     * datos
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Long idAsistencia;

    /**
     * Identificador del usuario que registra la asistencia. Se asume que este ID
     */
    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    /**
     * Sede a la que corresponde la asistencia
     */
    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Sede sede;

    /**
     * Fecha y hora de entrada del usuario al gimnasio
     */
    @Column(name = "fecha_hora_entrada", nullable = false)
    private LocalDateTime fechaHoraEntrada;

    /**
     * Tipo de acceso (entrada, salida, etc.)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_acceso", nullable = false)
    private EnumTipoAcceso tipoAcceso;

    /**
     * Estado del acceso (permitido, denegado, etc.)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_acceso", nullable = false)
    private EnumEstadoAcceso estadoAcceso;

    /**
    * Motivo de denegación del acceso (si aplica)
    */
    @Column(name = "motivo_denegacion", length = 255)
    private String motivoDenegacion;

    /**
     * Dispositivo utilizado para registrar la asistencia (opcional)
     */
    @Column(name = "dispositivo_id", length = 100)
    private String dispositivoId;
}