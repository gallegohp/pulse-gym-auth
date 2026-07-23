package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class PlanNutricionalGeneracionResponseDTO {

    /** ID del plan nutricional */
    private Long idPlanNutricional;

    /** Calorías diarias recomendadas */
    private Integer caloriasDiarias;

    /** Gramos de proteína diarios */
    private BigDecimal proteinasG;

    /** Gramos de carbohidratos diarios */
    private BigDecimal carbohidratosG;

    /** Gramos de grasas diarios */
    private BigDecimal grasasG;

    /** Restricciones dietéticas aplicadas */
    private List<String> restriccionesDieteticas;

    /** Sugerencias de comidas por tipo (desayuno, almuerzo, etc.) */
    private Map<String, List<SugerenciaComidaDTO>> sugerenciasComidas;

    /** Explicación generada por IA sobre el plan */
    private String explicacionIA;

    /** Versión del plan */
    private Integer version;

    /** Indica si fue generado por IA */
    private Boolean generadoPorIA;

    /** Fecha de generación del plan */
    private LocalDateTime fechaGeneracion;
}