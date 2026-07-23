package com.pulse_gym.ms_users.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
