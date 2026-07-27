package com.pulse_gym.lb_common.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.pulse_gym.lb_common.dto.PlanNutricionalGeneracionRequestDTO;
import com.pulse_gym.lb_common.dto.PlanNutricionalGeneracionResponseDTO;
import com.pulse_gym.lb_common.dto.RutinaGeneracionRequestDTO;
import com.pulse_gym.lb_common.dto.RutinaGeneracionResponseDTO;

@FeignClient(name = "pg-ms-ai", url = "${microservicio.ai.url:http://pg-ms-ai:8086}")
public interface AiClient {

    /**
     * Genera una rutina de entrenamiento personalizada usando IA
     * 
     * @param request Datos del socio y preferencias
     * @return Rutina generada con detalles de ejercicios
     */
    @PostMapping("/api/ai/generar-rutina")
    RutinaGeneracionResponseDTO generarRutina(@RequestBody RutinaGeneracionRequestDTO request);

    /**
     * Genera una rutina con contexto completo (para uso interno)
     * 
     * @param contexto Mapa con datos del socio y ejercicios
     * @return Rutina en formato JSON
     */
    @PostMapping("/api/ai/generar-rutina-contexto")
    String generarRutinaConContexto(@RequestBody Map<String, Object> contexto);

    /**
     * Verifica el estado del servicio de IA
     * 
     * @return Mapa con el estado del servicio
     */
    @PostMapping("/api/ai/health")
    Map<String, String> health();

    /**
     * Genera un plan nutricional personalizado usando IA
     * 
     * @param request Datos del socio y preferencias
     * @return Plan nutricional generado con sugerencias de comidas
     */
    @PostMapping("/api/ai/generar-plan-nutricional")
    PlanNutricionalGeneracionResponseDTO generarPlanNutricional(
            @RequestBody PlanNutricionalGeneracionRequestDTO request);

    /**
     * 
     * @param contexto
     * @return
     */
    @PostMapping("/api/ai/generar-plan-nutricional-contexto")
    String generarPlanNutricionalConContexto(@RequestBody Map<String, Object> contexto);
}