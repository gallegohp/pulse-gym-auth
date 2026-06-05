package com.pulse_gym.ms_notifications.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    /**
     * Logger para la clase
     */
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);

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
     * Método para enviar un email a un destinatario (texto plano - legacy)
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

    /**
     * Método para enviar un email en formato HTML con diseño profesional
     * @param destinatario Email del destinatario
     * @param asunto       Asunto del email
     * @param contenido   Contenido HTML del email (se insertará en el body)
     */
    public void enviarEmailHtml(String destinatario, String asunto, String contenido) {
        if (!emailEnabled) {
            logger.warn("Envío de emails deshabilitado. Email HTML no enviado a: {}", destinatario);
            return;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject(asunto);

            String htmlContent = generarPlantillaHtml(contenido);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("Email HTML enviado exitosamente a: {}", destinatario);

        } catch (MessagingException e) {
            logger.error("Error al enviar email HTML a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("No se pudo enviar el email HTML", e);
        }
    }

    /**
     * Genera la plantilla base HTML con diseño profesional
     * @param contenido Contenido principal del email
     * @return HTML completo con el diseño
     */
    private String generarPlantillaHtml(String contenido) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Pulse Gym</title>
                    <style>
                        body {
                            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                            line-height: 1.6;
                            color: #2c3e50;
                            margin: 0;
                            padding: 0;
                            background: linear-gradient(135deg, #e0eafc 0%%, #cfdef3 100%%);
                        }
                        .container {
                            max-width: 550px;
                            margin: 30px auto;
                            padding: 0;
                            background-color: #ffffff;
                            border-radius: 20px;
                            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
                            overflow: hidden;
                        }
                        .header {
                            background: linear-gradient(135deg, #2c4b77 0%%, #8bb5d6 100%%);
                            color: white;
                            padding: 35px 20px;
                            text-align: center;
                        }
                        .header h1 {
                            margin: 0;
                            font-size: 28px;
                            font-weight: 300;
                            letter-spacing: 1px;
                        }
                        .header p {
                            margin: 10px 0 0;
                            opacity: 0.9;
                            font-size: 14px;
                        }
                        .content {
                            padding: 40px 35px;
                            background-color: #ffffff;
                        }
                        .contenido-mensaje {
                            color: #5d6d7e;
                            font-size: 15px;
                            line-height: 1.6;
                        }
                        .footer {
                            background-color: #f8f9fc;
                            padding: 20px 30px;
                            text-align: center;
                            border-top: 1px solid #e8edf2;
                        }
                        .footer-text {
                            color: #9aabbb;
                            font-size: 11px;
                            margin: 5px 0;
                        }
                        .highlight {
                            color: #6c8ebf;
                            text-decoration: none;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1><strong>Pulse Gym</strong></h1>
                            <p>Tu bienestar, nuestra pasión</p>
                        </div>

                        <div class="content">
                            <div class="contenido-mensaje">
                                %s
                            </div>
                        </div>

                        <div class="footer">
                            <div class="footer-text">
                                © 2026 Pulse Gym - Todos los derechos reservados
                            </div>
                            <div class="footer-text">
                                Este es un mensaje automático, por favor no responder a este correo
                            </div>
                            <div class="footer-text">
                                <span class="highlight">Pulse Gym</span> - Donde los sueños se convierten en metas
                            </div>
                        </div>
                    </div>
                </body>
                </html>
                """, contenido);
    }
}