package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SugerenciaComidaDTO {

    /** Nombre de la comida o sugerencia */
    private String nombre;

    /** Descripción de la comida */
    private String descripcion;

    /** Calorías de la comida */
    private BigDecimal calorias;

    /** Proteínas de la comida */
    private BigDecimal proteinas;

    /** Carbohidratos de la comida */
    private BigDecimal carbohidratos;

    /** Grasas de la comida */
    private BigDecimal grasas;

    /** Ingredientes de la comida */
    private String ingredientes;

    /** Preparación de la comida */
    private String preparacion;
}