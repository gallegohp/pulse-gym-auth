package com.pulse_gym.ms_users.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse_gym.lb_common.client.AiClient;
import com.pulse_gym.lb_common.dto.DetalleRutinaResponseDTO;
import com.pulse_gym.lb_common.dto.RutinaGeneracionRequestDTO;
import com.pulse_gym.lb_common.dto.RutinaGeneracionResponseDTO;
import com.pulse_gym.lb_common.entity.user.DetalleRutina;
import com.pulse_gym.lb_common.entity.user.Ejercicio;
import com.pulse_gym.lb_common.entity.user.RutinaIA;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.ms_users.repository.DetalleRutinaRepository;
import com.pulse_gym.ms_users.repository.EjercicioRepository;
import com.pulse_gym.ms_users.repository.HistorialRutinaVersionRepository;
import com.pulse_gym.ms_users.repository.RutinaRepository;
import com.pulse_gym.ms_users.repository.SocioMembresiaRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RutinaService {

    /** Repositorio de rutinas */
    private final RutinaRepository rutinaRepository;

    /** Repositorio de detalles de rutina */
    private final DetalleRutinaRepository detalleRutinaRepository;

    /** Repositorio de historial de versiones de rutina */
    private final HistorialRutinaVersionRepository historialRutinaVersionRepository;

    /** Repositorio de usuarios */
    private final UsuarioPerfilRepository usuarioRepository;

    /** Repositorio de ejercicios */
    private final EjercicioRepository ejercicioRepository;

    /** Repositorio de membresías de socios */
    private final SocioMembresiaRepository socioMembresiaRepository;

    /** Servicio de generación de rutinas con IA */
    private final RutinaIAService rutinaIAService;

    /** Cliente Feign para consumir el servicio de IA */
    private final AiClient aiClient;

    /** Mapper para convertir objetos a JSON */
    private final ObjectMapper objectMapper;

    /**
     * Genera una rutina de entrenamiento usando IA
     * 
     * @param request           Preferencias para la generación de la rutina
     * @param userRol           Rol del usuario autenticado
     * @param userIdAutenticado ID del usuario autenticado
     * @return DTO con la rutina generada
     * @throws RuntimeException Si ocurre un error en la generación
     */
    @Transactional
    public RutinaGeneracionResponseDTO generarRutinaIA(
            RutinaGeneracionRequestDTO request,
            String userRol,
            Long userIdAutenticado) {

        log.info("Iniciando generación de rutina IA para socio ID: {}", request.getIdSocio());

        rutinaIAService.validarRolGeneracion(userRol, request.getIdSocio(), userIdAutenticado);

        rutinaIAService.validarMembresiaActiva(request.getIdSocio());

        UsuarioPerfil socio = usuarioRepository.findById(request.getIdSocio())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + request.getIdSocio()));

        Map<String, Object> contexto = rutinaIAService.construirContextoIA(request.getIdSocio(), request);

        RutinaGeneracionResponseDTO respuestaIA = null;
        try {
            respuestaIA = aiClient.generarRutina(request);
            log.info("Respuesta de IA recibida correctamente");
        } catch (Exception e) {
            log.error("Error al llamar al servicio de IA: {}", e.getMessage());
            throw new RuntimeException("Error al generar rutina con IA: " + e.getMessage());
        }

        RutinaIA rutina = guardarRutina(socio, respuestaIA, request);

        enriquecerConImagenes(respuestaIA);

        respuestaIA.setIdRutina(rutina.getIdRutinaIa());
        respuestaIA.setVersion(rutina.getVersion());
        respuestaIA.setFechaGeneracion(rutina.getFechaGeneracion());
        respuestaIA.setGeneradaPorIA(true);

        log.info("Rutina generada exitosamente con ID: {}, para socio: {}",
                rutina.getIdRutinaIa(), socio.getNombre());

        return respuestaIA;
    }

    /**
     * Guarda la rutina generada por IA en la base de datos
     * 
     * @param socio       Socio al que pertenece la rutina
     * @param respuestaIA Respuesta de la IA con los datos de la rutina
     * @param request     Preferencias del socio
     * @return Rutina guardada
     */
    private RutinaIA guardarRutina(UsuarioPerfil socio, RutinaGeneracionResponseDTO respuestaIA,
            RutinaGeneracionRequestDTO request) {

        RutinaIA rutina = new RutinaIA();
        rutina.setSocio(socio);
        rutina.setObjetivo(request.getObjetivoEspecifico() != null ? request.getObjetivoEspecifico()
                : socio.getObjetivoPrincipal());
        rutina.setNivel(socio.getNivelExperiencia().name());
        rutina.setCondiciones("Días por semana: " + request.getDiasPorSemana() +
                ", Duración: " + request.getDuracionSemanas() + " semanas");
        rutina.setModeloIa("OpenAI-GPT-4");
        rutina.setVersion(1);
        rutina.setActiva(true);
        rutina.setExplicacionIa(respuestaIA.getExplicacionIA());

        try {
            String rutinaJson = objectMapper.writeValueAsString(respuestaIA);
            rutina.setRutinaGenerada(rutinaJson);
        } catch (JsonProcessingException e) {
            log.warn("Error al serializar rutina a JSON: {}", e.getMessage());
            rutina.setRutinaGenerada(respuestaIA.toString());
        }

        rutina = rutinaRepository.save(rutina);

        if (respuestaIA.getDetalles() != null) {
            for (DetalleRutinaResponseDTO detalleDTO : respuestaIA.getDetalles()) {
                DetalleRutina detalle = new DetalleRutina();
                detalle.setRutinaIa(rutina);

                Ejercicio ejercicio = ejercicioRepository.findByNombreAndActivoTrue(detalleDTO.getNombreEjercicio())
                        .orElse(null);

                if (ejercicio == null) {
                    log.warn("Ejercicio no encontrado: {}, se omitirá", detalleDTO.getNombreEjercicio());
                    continue;
                }

                detalle.setEjercicio(ejercicio);
                detalle.setSeries(detalleDTO.getSeries() != null ? detalleDTO.getSeries() : 3);
                detalle.setRepeticionesMin(detalleDTO.getRepeticionesMin());
                detalle.setRepeticionesMax(detalleDTO.getRepeticionesMax());
                detalle.setPesoSugerido(detalleDTO.getPesoSugerido());
                detalle.setDescansoSegundos(detalleDTO.getDescansoSegundos());
                detalle.setDiaSemana(detalleDTO.getDiaSemana());
                detalle.setOrden(detalleDTO.getOrden());
                detalle.setNotas(detalleDTO.getNotas());

                detalleRutinaRepository.save(detalle);
            }
        }

        log.info("Rutina guardada con {} detalles", rutina.getDetalles().size());
        return rutina;
    }
}
