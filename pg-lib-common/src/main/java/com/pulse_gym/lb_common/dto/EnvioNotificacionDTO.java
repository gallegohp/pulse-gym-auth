package com.pulse_gym.lb_common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioNotificacionDTO {
    
    @NotNull(message = "El ID de usuario es requerido")
    private Long usuarioId;
    
    @NotBlank(message = "El destinatario es requerido")
    private String destinatario; // email o número de WhatsApp
    
    private String asunto;
    
    @NotBlank(message = "El contenido es requerido")
    private String contenido;
    
    @NotNull(message = "El canal es requerido")
    private String canal; // "EMAIL" o "WHATSAPP"
    
    private String tipoEvento;
    private Long plantillaId;
}