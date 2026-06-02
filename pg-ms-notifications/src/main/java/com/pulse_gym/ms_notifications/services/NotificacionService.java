package com.pulse_gym.ms_notifications.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.EnvioNotificacionDTO;
import com.pulse_gym.lb_common.entity.notification.Notificacion;
import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;
import com.pulse_gym.lb_common.enums.EnumEstadoNotificacion;
import com.pulse_gym.ms_notifications.repository.NotificacionRepository;
import com.pulse_gym.ms_notifications.repository.PlantillaNotificationRepository;

import java.time.LocalDateTime;

@Service
public class NotificacionService {
    
    /**
     * Logger para la clase
     */
    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);
    
    /**
     * Inyeccion de EmailService para enviar emails
     */ 
    @Autowired
    private EmailService emailService;
    
    /**
     * Inyeccion de NotificacionRepository para manejar las operaciones de base de datos
     * relacionadas con las notificaciones
     */
    @Autowired
    private NotificacionRepository notificacionRepository;
    
    /**
     * Inyeccion de PlantillaNotificationRepository para manejar las operaciones de base de datos
     * relacionadas con las plantillas de notificaciones
     */
    @Autowired
    private PlantillaNotificationRepository plantillaRepository;
    
    /**
     * Método para enviar una notificación a un usuario específico o a un grupo de usuarios.
     * @param dto Objeto con los datos necesarios para enviar la notificación
     */ 
    public void enviarNotificacion(EnvioNotificacionDTO dto) {
        logger.info("Enviando notificación a {} por canal {}", dto.getDestinatario(), dto.getCanal());
        
        PlantillaNotificacion plantilla = null;
        if (dto.getPlantillaId() != null) {
            plantilla = plantillaRepository.findById(dto.getPlantillaId()).orElse(null);
        }
        
        Notificacion notificacion = new Notificacion();
        notificacion.setId_usuario(dto.getUsuarioId());
        notificacion.setId_plantilla(plantilla);
        notificacion.setTitulo(dto.getAsunto() != null ? dto.getAsunto() : "Notificación Pulse Gym");
        notificacion.setContenido(dto.getContenido());
        notificacion.setEstado(EnumEstadoNotificacion.PENDIENTE);  
        notificacion.setFechaEnvio(LocalDateTime.now());
        
        notificacionRepository.save(notificacion);
        
        try {
            if ("EMAIL".equalsIgnoreCase(dto.getCanal())) {
                emailService.enviarEmail(
                    dto.getDestinatario(),
                    dto.getAsunto() != null ? dto.getAsunto() : "Notificación Pulse Gym",
                    dto.getContenido()
                );
                notificacion.setEstado(EnumEstadoNotificacion.ENVIADO);
            } else if ("WHATSAPP".equalsIgnoreCase(dto.getCanal())) {
                logger.warn("WhatsApp aún no implementado");
                notificacion.setEstado(EnumEstadoNotificacion.RECHAZADO); 
                throw new RuntimeException("WhatsApp no implementado");
            }
            
            notificacionRepository.save(notificacion);
            logger.info("Notificación enviada exitosamente a {}", dto.getDestinatario());
            
        } catch (Exception e) {
            logger.error("Error al enviar notificación: {}", e.getMessage());
            notificacion.setEstado(EnumEstadoNotificacion.RECHAZADO);  
            notificacionRepository.save(notificacion);
            throw new RuntimeException("Error al enviar notificación: " + e.getMessage(), e);
        }
    }
}