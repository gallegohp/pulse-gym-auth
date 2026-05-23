package com.pulse_gym.ms_gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.pulse_gym.lb_common.services.JwtService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class JwtValidationFilter implements GlobalFilter, Ordered{
    
    /**
     * Inyeccion de jwtService para manejar la validacion de tokens jwt
     */
    private final JwtService jwtService;

    /**
     * Este método es el núcleo del filtro de validación JWT. Se encarga de interceptar cada solicitud entrante y verificar si la ruta es pública 
     * o si el método HTTP es OPTIONS, en cuyo caso permite que la solicitud continúe sin validación. Para las rutas protegidas, extrae el token 
     * JWT del encabezado Authorization, valida su formato y verifica su validez utilizando el servicio JwtService. Si el token es válido, extrae 
     * la información del usuario (ID, rol y nombre de usuario) del token y agrega esta información como encabezados personalizados a la solicitud 
     * antes de continuar con la cadena de filtros. Si el token es inválido o falta, responde con un error 401 Unauthorized y un mensaje JSON que describe el error.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (isPublicPath(path) || exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }

        List<String> authHeaders = exchange.getRequest().getHeaders().getOrEmpty(HttpHeaders.AUTHORIZATION);
        if (authHeaders.isEmpty() || !authHeaders.get(0).startsWith("Bearer ")) {
            return unauthorized(exchange, "Header Authorization missing or invalid");
        }

        String token = authHeaders.get(0).substring(7);
        if (!jwtService.isTokenValid(token)) {
            return unauthorized(exchange, "Token invalido o expirado");
        }

        Long userId = jwtService.extractUserId(token);
        String rol = jwtService.extractRol(token);  
        String username = jwtService.extractUsername(token);
        
        System.out.println("userId: " + userId);
        System.out.println("rol: " + rol);
        System.out.println("username: " + username);

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-User-Id", userId != null ? userId.toString() : "")
                .header("X-User-Name", username != null ? username : "") 
                .header("X-User-Rol", rol != null ? rol : "") 
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        return chain.filter(mutatedExchange);
    }

    /**
     * Verifica si la ruta solicitada es una ruta pública que no requiere autenticación.
     * @param path
     * @return true si la ruta es pública, false si la ruta requiere autenticación
     */
    private boolean isPublicPath(String path) {
        return path.startsWith("/pg-ms-auth/auth/login") 
                || path.startsWith("/pg-ms-auth/auth/register")
                || path.startsWith("/pg-ms-auth/auth/refresh");
    }

    /**
     * Responde a la solicitud con un error 401 Unauthorized y un mensaje JSON que describe el error. Este método se utiliza para manejar casos en los 
     * que el token JWT es inválido, ha expirado o falta en la solicitud.
     * @param exchange
     * @param message
     * @return un Mono<Void> que representa la respuesta de error enviada al cliente
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(("{\"error\": \"" + message + "\"}").getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
    
    /**
     * Especifica el orden de ejecución del filtro en la cadena de filtros de Spring Cloud Gateway.
     * HIGHEST_PRECEDENCE asegura que este filtro se ejecute antes que cualquier otro filtro
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}