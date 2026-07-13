package com.pulse_gym.lb_common.config;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    /** Nombre del header de autorización */
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /** Prefijo del token Bearer */
    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    /**
     * Intercepta y propaga los headers de autenticación y contexto del usuario
     * desde la petición original hacia las llamadas Feign
     */
    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            String token = request.getHeader(AUTHORIZATION_HEADER);
            if (token != null && !token.isEmpty()) {
                template.header(AUTHORIZATION_HEADER, token);
            }

            String userRol = request.getHeader("X-User-Rol");
            if (userRol != null && !userRol.isEmpty()) {
                template.header("X-User-Rol", userRol);
            }

            String userId = request.getHeader("X-User-Id");
            if (userId != null && !userId.isEmpty()) {
                template.header("X-User-Id", userId);
            }

            String userEmail = request.getHeader("X-User-Email");
            if (userEmail != null && !userEmail.isEmpty()) {
                template.header("X-User-Email", userEmail);
            }
        }
    }
}