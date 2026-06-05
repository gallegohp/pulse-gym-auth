package com.pulse_gym.ms_notifications.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    /**
     * Logger para la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    /**
     * Inyeccion de JavaMailSender para enviar emails
     */
    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * Email del remitente
     */
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    /**
     * Indica si el envío de emails está habilitado
     */
    @Value("${notificaciones.email.enabled:true}")
    private boolean emailEnabled;
    
    /**
     * Método para enviar un email a un destinatario
     * @param destinatario Email del destinatario
     * @param asunto       Asunto del email
     * @param contenido    Contenido del email
     */
    public void enviarEmail(String destinatario, String asunto, String contenido) {
        if (!emailEnabled) {
            logger.warn("Envío de emails deshabilitado. Email no enviado a: {}", destinatario);
            return;
        }
        
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject(asunto);
            mensaje.setText(contenido);
            mensaje.setFrom(fromEmail);
            
            mailSender.send(mensaje);
            logger.info("Email enviado exitosamente a: {}", destinatario);
            
        } catch (Exception e) {
            logger.error("Error al enviar email a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }
}