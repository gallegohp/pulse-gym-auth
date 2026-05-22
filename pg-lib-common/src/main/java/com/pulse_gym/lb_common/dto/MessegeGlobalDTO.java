
package com.pulse_gym.lb_common.dto;

import lombok.Data;

@Data
public class MessegeGlobalDTO {
    
    /**
     * Mensaje global para respuestas de error o exito
     */
    private String message;

    /**
     * Constructor que acepta un mensaje como parámetro
     */
    public MessegeGlobalDTO(String message) {
        this.message = message;
    }
}
