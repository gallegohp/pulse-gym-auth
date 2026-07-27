package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PlanNutricionalGeneracionResponseDTO {

    /** ID del plan nutricional */
    private Long idPlanNutricional;

    /** Calorías diarias recomendadas */
    @JsonProperty("calorias_diarias")
    private Integer caloriasDiarias;

    /** Gramos de proteína diarios */
    @JsonProperty("proteinas_g")
    private BigDecimal proteinasG;

    /** Gramos de carbohidratos diarios */
    @JsonProperty("carbohidratos_g")
    private BigDecimal carbohidratosG;

    /** Gramos de grasas diarios */
    @JsonProperty("grasas_g")
    private BigDecimal grasasG;

    /** Restricciones dietéticas aplicadas */
    private List<String> restriccionesDieteticas;

    /** Sugerencias de comidas por tipo (desayuno, almuerzo, etc.) */
    @JsonProperty("sugerencias_comidas")
    private Map<String, List<SugerenciaComidaDTO>> sugerenciasComidas;

    /** Explicación generada por IA sobre el plan */
    @JsonProperty("explicacion_ia")
    private String explicacionIA;

    /** Versión del plan */
    private Integer version;

    /** Indica si fue generado por IA */
    private Boolean generadoPorIA;

    /** Fecha de generación del plan */
    private LocalDateTime fechaGeneracion;
}
