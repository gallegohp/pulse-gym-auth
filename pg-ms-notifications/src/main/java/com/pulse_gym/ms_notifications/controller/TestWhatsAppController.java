package com.pulse_gym.ms_notifications.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.ms_notifications.services.WhatsAppService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestWhatsAppController {

    private final WhatsAppService whatsAppService;

    @GetMapping("/test-whatsapp")
    public String enviarPrueba() {

        whatsAppService.enviarWhatsApp(
                "whatsapp:+573001112233",
                "Prueba Twilio desde Pulse Gym");

        return "Mensaje enviado";
    }
}