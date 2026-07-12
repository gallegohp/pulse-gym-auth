package com.pulse_gym.ms_users.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.EjercicioRequestDTO;
import com.pulse_gym.lb_common.dto.EjercicioResponseDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.user.Ejercicio;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
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

    /**
     * Crea un nuevo ejercicio
     * 
     * @param request DTO con los datos del ejercicio
     * @param userRol Rol del usuario autenticado
     * @return Mensaje de confirmación
     * @throws RuntimeException Si el grupo muscular no es válido, ya existe un
     *                          ejercicio con ese nombre
     *                          o el equipo no existe en el sistema
     */
    @Transactional
    public MessegeGlobalDTO crearEjercicio(EjercicioRequestDTO request, String userRol) {
        ValidacionDeRoles.validarAdminOEntrenador(userRol);

        if (!GRUPOS_MUSCULARES.contains(request.getGrupoMuscular().toUpperCase())) {
            throw new RuntimeException("Grupo muscular no válido. Valores permitidos: " + GRUPOS_MUSCULARES);
        }

        if (ejercicioRepository.existsByNombreAndActivoTrue(request.getNombre())) {
            throw new RuntimeException("Ya existe un ejercicio activo con el nombre: " + request.getNombre());
        }

        equipoValidationService.validarEquipoExistenteOrThrow(request.getEquipoNecesario());

        Ejercicio ejercicio = new Ejercicio();
        ejercicio.setNombre(request.getNombre());
        ejercicio.setGrupoMuscular(request.getGrupoMuscular().toUpperCase());
        ejercicio.setEquipoNecesario(request.getEquipoNecesario());
        ejercicio.setExplicacionTecnica(request.getExplicacionTecnica());
        ejercicio.setUrlImagen(request.getUrlImagen());
        ejercicio.setDificultad(request.getDificultad());
        ejercicio.setCaloriasPorMinuto(request.getCaloriasPorMinuto());
        ejercicio.setUrlVideo(request.getUrlVideo());
        ejercicio.setActivo(request.getActivo() != null ? request.getActivo() : true);

        ejercicioRepository.save(ejercicio);

        return new MessegeGlobalDTO("Ejercicio '" + ejercicio.getNombre() + "' creado correctamente");
    }
}
