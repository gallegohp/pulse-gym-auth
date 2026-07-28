package com.pulse_gym.lb_common.entity.operation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_biometrica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaBiometrica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "hash_huella", length = 64)
    private String hashHuella; 

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "exitoso", nullable = false)
    private Boolean exitoso;

    @Column(name = "mensaje", length = 255)
    private String mensaje;

    @Column(name = "id_sede")
    private Long idSede;

    @Column(name = "tipo_log", length = 20)
    private String tipoLog = "HUELLA"; 
}