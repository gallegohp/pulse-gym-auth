package com.pulse_gym.ms_users.service;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Service;

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
public class PlanNutricionalIAService {

    /** Repositorio de usuarios */
    private final UsuarioPerfilRepository usuarioRepository;

    /** Repositorio de perfiles médicos */
    private final PerfilMedicoRepository perfilMedicoRepository;

    /** Repositorio de historial físico */
    private final HistorialFisicoRepository historialFisicoRepository;

    /** Repositorio de rutinas */
    private final RutinaRepository rutinaRepository;

    /** Repositorio de membresías de socios */
    private final SocioMembresiaRepository socioMembresiaRepository;

    /**
     * Calcula la edad a partir de la fecha de nacimiento
     * 
     * @param fechaNacimiento Fecha de nacimiento
     * @return Edad en años, o 0 si la fecha es nula
     */
    private int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null)
            return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
