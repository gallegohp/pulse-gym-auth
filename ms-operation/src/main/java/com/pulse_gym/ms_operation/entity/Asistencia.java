package com.pulse_gym.ms_operation.entity;

import java.time.LocalDateTime;

import com.pulse_gym.ms_operation.enums.EnumEstadoAcceso;
import com.pulse_gym.ms_operation.enums.EnumTipoAcceso;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asistencia")
    private Long idAsistencia;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Sede sede;

    @Column(name = "fecha_hora_entrada", nullable = false)
    private LocalDateTime fechaHoraEntrada;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_acceso", nullable = false)
    private EnumTipoAcceso tipoAcceso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_acceso", nullable = false)
    private EnumEstadoAcceso estadoAcceso;

    @Column(name = "motivo_denegacion", length = 255)
    private String motivoDenegacion;

    @Column(name = "dispositivo_id", length = 100)
    private String dispositivoId;
}