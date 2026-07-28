package com.pulse_gym.lb_common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;

import io.jsonwebtoken.JwtException;
import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    /**
     * Maneja los errores de validación de campos en el cuerpo de las solicitudes
     * (fieldName y errorMessage)
     * Se activa cuando un DTO no cumple las restricciones definidas con anotaciones
     * de validación.
     * 
     * @param ex excepción con los detalles de los campos que fallaron en la
     *           validación
     * @return mapa con el nombre de cada campo inválido y su mensaje de error con
     *         estado 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Maneja las excepciones de negocio que vienen de los service y controller
     * 
     * @param ex excepcion con el mensaje del error
     * @return mensaje de error con estado (400)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessegeGlobalDTO> handleRuntimeException(RuntimeException ex) {
        log.warn("Excepción de negocio: {}", ex.getMessage());
        MessegeGlobalDTO response = new MessegeGlobalDTO(ex.getMessage());
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = ex.getMessage();

        if (msg != null) {
            String lowerMsg = msg.toLowerCase();
            // Token / autenticación
            if (lowerMsg.contains("token") || lowerMsg.contains("jwt") ||
                    lowerMsg.contains("expirado") || lowerMsg.contains("no corresponde")) {
                status = HttpStatus.UNAUTHORIZED;
            }
            // Huella / biometría
            else if (lowerMsg.contains("huella") && lowerMsg.contains("no reconocida")) {
                status = HttpStatus.UNAUTHORIZED;
            }
            // Membresía
            else if (lowerMsg.contains("membresía")
                    && (lowerMsg.contains("vencida") || lowerMsg.contains("inactiva"))) {
                status = HttpStatus.FORBIDDEN;
            }
            // No encontrado
            else if (lowerMsg.contains("no encontrado") || lowerMsg.contains("no existe")) {
                status = HttpStatus.NOT_FOUND;
            }
            // Conflictos (ej. ya existe)
            else if (lowerMsg.contains("ya existe") || lowerMsg.contains("duplicado")) {
                status = HttpStatus.CONFLICT;
            }
        }

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Maneja los errores sobre JWT, token expirado o inválido o malformado
     *
     * @param ex excepcion con el detalle del fallo en la validación del token
     * @return mensaje de error con estado (401)
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<MessegeGlobalDTO> handleJwtException(JwtException ex) {
        log.warn("Error de JWT: {}", ex.getMessage());
        MessegeGlobalDTO response = new MessegeGlobalDTO("Token inválido: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Maneja errores de autorización (Roles no permitidos)
     *
     * @param ex
     * @param ex excepción de seguridad
     * @return un estado 403 FORBIDDEN
     */
    @ExceptionHandler(SecurityAuthorizationException.class)
    public ResponseEntity<MessegeGlobalDTO> handleSecurityException(SecurityAuthorizationException ex) {
        log.warn("Error de autorización: {}", ex.getMessage());
        MessegeGlobalDTO response = new MessegeGlobalDTO(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

}