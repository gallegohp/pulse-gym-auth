package com.pulse_gym.ms_users.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pulse_gym.lb_common.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtContextFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                if (jwtService.isTokenValid(token)) {
                    Long userId = jwtService.extractUserId(token);
                    String rol = jwtService.extractRol(token);  // Ahora es String directamente
                    String username = jwtService.extractUsername(token);
                    
                    System.out.println("=== FILTER: Extrayendo del token ===");
                    System.out.println("userId: " + userId);
                    System.out.println("rol: " + rol);
                    System.out.println("username: " + username);
                    
                    request.setAttribute("X-User-Id", userId);
                    request.setAttribute("X-User-Rol", rol);  // El rol ya viene como String
                    request.setAttribute("X-User-Name", username);
                }
            }
        } catch (Exception e) {
            log.error("Error procesando token en filtro: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
}