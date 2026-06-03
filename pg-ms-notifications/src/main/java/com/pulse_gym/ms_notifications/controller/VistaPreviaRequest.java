package com.pulse_gym.ms_notifications.controller;

import java.util.Map;

import lombok.Data;

@Data
public class VistaPreviaRequest {

    /**
     * Contenido de la plantilla con variables
     */
    private String contenido;

    /**
     * Valores de prueba opcionales para la vista previa
     */
    private Map<String, Object> valoresPrueba;
}
