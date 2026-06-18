package com.pulse_gym.ms_users.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mercadopago.MercadoPagoConfig;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MercadoPagoConfiguration {

    @Value("${payment.mercadopago.access-token}")
    private String accessToken;

    @Value("${payment.mercadopago.environment:sandbox}")
    private String environment;

    @PostConstruct
    public void init() {
        // Usar la clase del SDK directamente
        MercadoPagoConfig.setAccessToken(accessToken);
        
        log.info("✅ MercadoPago configurado correctamente en modo: {}", environment);
        log.info("🔑 Access Token configurado: {}", accessToken.substring(0, 10) + "...");
    }
}