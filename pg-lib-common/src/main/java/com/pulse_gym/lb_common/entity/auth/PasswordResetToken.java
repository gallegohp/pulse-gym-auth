package com.pulse_gym.lb_common.entity.auth;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "password_reset_tokens")
public class PasswordResetToken {

    /**
     * Identificador único del token (autogenerado)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Token único de recuperación (formato UUID)
     */
    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    /**
     * Usuario asociado a este token de recuperación
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Fecha y hora de expiración del token
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Indica si el token ya fue utilizado
     */
    @Column(name = "used", nullable = false)
    private boolean used = false;

    /**
     * Verifica si el token ha expirado
     * 
     * @return true si la fecha actual es posterior a la fecha de expiración
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}