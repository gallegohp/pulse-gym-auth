package com.pulse_gym.ms_auth.dto;

import lombok.Data;

@Data
public class HttpGlobalResponse<T> {
    /**
     * Objeto que contiene una data
     */
    private T data;

    /**
     * Mensaje de respuesta
     */
    private String messege;
}