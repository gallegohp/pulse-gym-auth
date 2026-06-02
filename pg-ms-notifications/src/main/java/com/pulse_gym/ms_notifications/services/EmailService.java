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
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    @Value("${notificaciones.email.enabled:true}")
    private boolean emailEnabled;
    
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