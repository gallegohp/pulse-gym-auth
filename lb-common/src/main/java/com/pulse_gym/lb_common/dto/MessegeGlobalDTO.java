
package com.pulse_gym.lb_common.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class MessegeGlobalDTO {
    
    /**
     * Mensaje global para respuestas de error o exito
     */
    private String message;
}
