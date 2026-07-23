package com.pulse_gym.ms_users.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pulse_gym.lb_common.client.AiClient;
import com.pulse_gym.lb_common.dto.PlanNutricionalGeneracionRequestDTO;
import com.pulse_gym.lb_common.dto.PlanNutricionalGeneracionResponseDTO;
import com.pulse_gym.lb_common.entity.user.PlanNutricionalIA;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.ms_users.repository.PlanNutricionalRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanNutricionalService {

    /** Repositorio de planes nutricionales */
    private final PlanNutricionalRepository planNutricionalRepository;

    /** Repositorio de usuarios */
    private final UsuarioPerfilRepository usuarioRepository;

    /** Servicio de generación de planes nutricionales con IA */
    private final PlanNutricionalIAService planNutricionalIAService;

    /** Cliente Feign para consumir el servicio de IA */
    private final AiClient aiClient;

    /** Mapper para convertir objetos a JSON */
    private final ObjectMapper objectMapper;

    /**
     * 
     * Genera un plan nutricional usando IA
     * 
     * @param request           Preferencias para la generación del plan
     * @param userRol           Rol del usuario autenticado
     * @param userIdAutenticado ID del usuario autenticado
     * @param userEmail         Email del usuario autenticado
     * @return DTO con el plan nutricional generado
     */
    @Transactional
    public PlanNutricionalGeneracionResponseDTO generarPlanNutricional(
            PlanNutricionalGeneracionRequestDTO request,
            String userRol,
            Long userIdAutenticado,
            String userEmail) {

        log.info("Generando plan nutricional para socio ID: {}", request.getIdSocio());

        UsuarioPerfil socio;
        if ("socio".equals(userRol)) {
            socio = usuarioRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Socio no encontrado con email: " + userEmail));

            request.setIdSocio(socio.getIdUsuario());
            log.info("Socio autenticado por email: {}, ID en usuario_perfil: {}", userEmail, socio.getIdUsuario());
        } else {
            socio = usuarioRepository.findById(request.getIdSocio())
                    .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + request.getIdSocio()));
        }

        planNutricionalIAService.validarRolGeneracion(userRol, request.getIdSocio(), userIdAutenticado, userEmail);
        planNutricionalIAService.validarMembresiaActiva(request.getIdSocio());

        Map<String, Object> contexto = planNutricionalIAService.construirContextoIA(request.getIdSocio(), request);

        PlanNutricionalGeneracionResponseDTO respuestaIA = null;
        try {
            String respuestaJson = aiClient.generarRutinaConContexto(contexto);

            log.info("JSON recibido de Python (primeros 300 chars): {}",
                    respuestaJson.length() > 300 ? respuestaJson.substring(0, 300) + "..." : respuestaJson);

            respuestaIA = objectMapper.readValue(respuestaJson, PlanNutricionalGeneracionResponseDTO.class);

            log.info("Plan nutricional generado correctamente");
        } catch (Exception e) {
            log.error("Error al llamar al servicio de IA: {}", e.getMessage());
            throw new RuntimeException("Error al generar plan nutricional con IA: " + e.getMessage());
        }

        PlanNutricionalIA plan = guardarPlan(socio, respuestaIA, request);

        respuestaIA.setIdPlanNutricional(plan.getIdPlanNutricional());
        respuestaIA.setVersion(plan.getVersion());
        respuestaIA.setFechaGeneracion(plan.getFechaGeneracion());
        respuestaIA.setGeneradoPorIA(true);

        log.info("Plan nutricional generado exitosamente con ID: {}, para socio: {}",
                plan.getIdPlanNutricional(), socio.getNombre());

        return respuestaIA;
    }

    /**
     * 
     * Guarda el plan nutricional generado por IA en la base de datos
     * 
     * @param socio       Socio al que pertenece el plan
     * @param respuestaIA Respuesta de la IA con los datos del plan
     * @param request     Preferencias del socio
     * @return Plan nutricional guardado
     */
    private PlanNutricionalIA guardarPlan(UsuarioPerfil socio,
            PlanNutricionalGeneracionResponseDTO respuestaIA,
            PlanNutricionalGeneracionRequestDTO request) {

        PlanNutricionalIA plan = new PlanNutricionalIA();
        plan.setSocio(socio);
        plan.setCaloriasDiarias(respuestaIA.getCaloriasDiarias());
        plan.setProteinasG(respuestaIA.getProteinasG());
        plan.setCarbohidratosG(respuestaIA.getCarbohidratosG());
        plan.setGrasasG(respuestaIA.getGrasasG());

        if (request.getRestriccionesDieteticas() != null && !request.getRestriccionesDieteticas().isEmpty()) {
            plan.setRestriccionesDieteticas(String.join(", ", request.getRestriccionesDieteticas()));
        }

        try {
            if (respuestaIA.getSugerenciasComidas() != null) {
                String sugerenciasJson = objectMapper.writeValueAsString(respuestaIA.getSugerenciasComidas());
                plan.setSugerenciasComidas(sugerenciasJson);
            }
        } catch (JsonProcessingException e) {
            log.warn("Error al serializar sugerencias de comidas: {}", e.getMessage());
        }

        try {
            String planJson = objectMapper.writeValueAsString(respuestaIA);
            plan.setPlanGenerado(planJson);
        } catch (JsonProcessingException e) {
            log.warn("Error al serializar plan nutricional: {}", e.getMessage());
            plan.setPlanGenerado(respuestaIA.toString());
        }

        plan.setModeloIa("Gemini-2.5-Flash");
        plan.setVersion(1);
        plan.setActivo(true);

        List<PlanNutricionalIA> planesAnteriores = planNutricionalRepository
                .findBySocio_IdUsuarioAndActivoTrue(socio.getIdUsuario());
        for (PlanNutricionalIA p : planesAnteriores) {
            p.setActivo(false);
        }
        if (!planesAnteriores.isEmpty()) {
            planNutricionalRepository.saveAll(planesAnteriores);
        }

        return planNutricionalRepository.save(plan);
    }
}
