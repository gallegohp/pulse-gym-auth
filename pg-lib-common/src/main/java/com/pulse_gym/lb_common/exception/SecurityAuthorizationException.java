package com.pulse_gym.lb_common.exception;

public class SecurityAuthorizationException extends RuntimeException {

    /**
     * Excepción personalizada para indicar que un usuario no tiene autorización para acceder a un recurso o realizar una acción específica
     * @param message mensaje de error que describe la razón de la falta de autorización
     */
    public SecurityAuthorizationException(String message) {
        super(message);
    }

}