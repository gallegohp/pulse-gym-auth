package com.pulse_gym.ms_users.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse_gym.lb_common.client.AiClient;
import com.pulse_gym.ms_users.repository.DetalleRutinaRepository;
import com.pulse_gym.ms_users.repository.EjercicioRepository;
import com.pulse_gym.ms_users.repository.HistorialFisicoRepository;
import com.pulse_gym.ms_users.repository.PerfilMedicoRepository;
import com.pulse_gym.ms_users.repository.RutinaRepository;
import com.pulse_gym.ms_users.repository.SocioMembresiaRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RutinaIAService {

    /** Repositorio de usuarios */
    private final UsuarioPerfilRepository usuarioRepository;

    /** Repositorio de perfiles médicos */
    private final PerfilMedicoRepository perfilMedicoRepository;

    /** Repositorio de historial físico */
    private final HistorialFisicoRepository historialFisicoRepository;

    /** Repositorio de ejercicios */
    private final EjercicioRepository ejercicioRepository;

    /** Repositorio de membresías de socios */
    private final SocioMembresiaRepository socioMembresiaRepository;

    /** Repositorio de rutinas */
    private final RutinaRepository rutinaRepository;

    /** Repositorio de detalles de rutina */
    private final DetalleRutinaRepository detalleRutinaRepository;

    /** Cliente Feign para consumir el servicio de IA */
    private final AiClient aiClient;

    /** Mapper para convertir objetos a JSON */
    private final ObjectMapper objectMapper;
}
