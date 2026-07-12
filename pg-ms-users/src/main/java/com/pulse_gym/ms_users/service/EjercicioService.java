package com.pulse_gym.ms_users.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.EjercicioResponseDTO;
import com.pulse_gym.lb_common.entity.user.Ejercicio;
import com.pulse_gym.ms_users.repository.EjercicioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EjercicioService {

    /** Repositorio para operaciones CRUD de ejercicios */
    private final EjercicioRepository ejercicioRepository;

    /** Servicio para validar equipos contra pg-ms-operation */
    private final EquipoValidationService equipoValidationService;

    /**
     * Lista de grupos musculares válidos según el diseño de BD
     */
    private static final List<String> GRUPOS_MUSCULARES = List.of(
            "PECHO", "ESPALDA", "PIERNA", "HOMBRO", "BRAZO", "CORE", "CARDIO");

    /**
     * Convierte una entidad Ejercicio a EjercicioResponseDTO
     * 
     * @param ejercicio Entidad a convertir
     * @return DTO del ejercicio
     */
    private EjercicioResponseDTO convertirAResponseDTO(Ejercicio ejercicio) {
        EjercicioResponseDTO dto = new EjercicioResponseDTO();
        dto.setIdEjercicio(ejercicio.getIdEjercicio());
        dto.setNombre(ejercicio.getNombre());
        dto.setGrupoMuscular(ejercicio.getGrupoMuscular());
        dto.setEquipoNecesario(ejercicio.getEquipoNecesario());
        dto.setExplicacionTecnica(ejercicio.getExplicacionTecnica());
        dto.setUrlImagen(ejercicio.getUrlImagen());
        dto.setDificultad(ejercicio.getDificultad());
        dto.setCaloriasPorMinuto(ejercicio.getCaloriasPorMinuto());
        dto.setUrlVideo(ejercicio.getUrlVideo());
        dto.setActivo(ejercicio.getActivo());
        return dto;
    }
}
