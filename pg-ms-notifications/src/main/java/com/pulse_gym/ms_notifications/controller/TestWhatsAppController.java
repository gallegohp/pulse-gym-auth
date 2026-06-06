package com.pulse_gym.ms_notifications.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.ms_notifications.services.WhatsAppService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestWhatsAppController {

    /**
     * Inyeccion de servicio de WhatsApp
     */
    private final WhatsAppService whatsAppService;

    /**
     * Envia un mensaje de prueba a un usuario de WhatsApp
     *
     * @return Mensaje de prueba
     */
    @GetMapping("/test-whatsapp")
    public String enviarPrueba() {

        whatsAppService.enviarWhatsApp(
                "whatsapp:+573248589488",
                "Prueba Twilio desde Pulse Gym");

        return "Mensaje enviado";
    }
}