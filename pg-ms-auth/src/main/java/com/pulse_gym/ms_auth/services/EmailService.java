package com.pulse_gym.ms_auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    /** Cliente para enviar correos electrónicos */
    private final JavaMailSender mailSender;

    /** Correo electrónico remitente configurado en application.yaml */
    @Value("${spring.mail.username}")
    private String fromEmail;

    // @Value("${app.frontend-url:http://localhost:3000}")
    // private String frontendUrl;

    /**
     * Envía un correo electrónico con el token para restablecer la contraseña
     * 
     * @param to       Correo electrónico del destinatario
     * @param username Nombre del usuario que solicita el cambio
     * @param token    Token único para restablecer la contraseña
     */
    public void sendPasswordResetEmailSimple(String to, String username, String token) {
        String resetLink = "\nIngresa este token : \n \n" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject("Restablecimiento de contraseña - Pulse Gym");
        message.setText(String.format("""
                Hola %s,

                Hemos recibido una solicitud para restablecer tu contraseña.

                Para continuar, copia este enlace en tu navegador:
                %s

                Este enlace expirará en 1 minutos.

                Si no solicitaste este cambio, ignora este mensaje.

                Saludos,
                Equipo de Pulse Gym
                """, username, resetLink));

        mailSender.send(message);
    }

}
