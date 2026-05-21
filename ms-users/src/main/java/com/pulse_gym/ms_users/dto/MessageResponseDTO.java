package com.pulse_gym.ms_users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageResponseDTO {

    /**
     * Mensaje descriptivo sobre el resultado de la operación realizada (ej. éxito o
     * error)
     */
    private String message;
}