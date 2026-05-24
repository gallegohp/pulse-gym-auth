package com.pulse_gym.lb_common.exception;

public class SecurityAuthorizationException extends RuntimeException {

    /**
     * Crea una excepción de autorización con un mensaje específico
     * 
     * @param message Mensaje descriptivo del error de autorización
     */
    public SecurityAuthorizationException(String message) {
        super(message);
    }

}