package com.pulse_gym.lb_common.entity.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "plan_nutricional_ia")
@Data
public class PlanNutricionalIA {

    /** Identificador único del plan nutricional */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plan_nutricional")
    private Long idPlanNutricional;

    /** Socio al que pertenece el plan nutricional */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_socio", nullable = false)
    private UsuarioPerfil socio;

    /** Rutina asociada al plan nutricional (opcional) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_rutina_ia")
    private RutinaIA rutina;

    /** Calorías diarias recomendadas */
    @Column(name = "calorias_diarias", nullable = false)
    private Integer caloriasDiarias;

    /** Gramos de proteína diarios */
    @Column(name = "proteinas_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal proteinasG;

    /** Gramos de carbohidratos diarios */
    @Column(name = "carbohidratos_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal carbohidratosG;

    /** Gramos de grasas diarios */
    @Column(name = "grasas_g", nullable = false, precision = 10, scale = 2)
    private BigDecimal grasasG;

    /** Restricciones dietéticas del socio */
    @Column(name = "restricciones_dieteticas", columnDefinition = "TEXT")
    private String restriccionesDieteticas;

    /** Sugerencias de comidas para el plan */
    @Column(name = "sugerencias_comidas", columnDefinition = "TEXT")
    private String sugerenciasComidas;

    /** Plan generado en formato estructurado (JSON/Texto) */
    @Column(name = "plan_generado", columnDefinition = "TEXT")
    private String planGenerado;

    /** Fecha de generación del plan */
    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    /** Modelo de IA utilizado para la generación */
    @Column(name = "modelo_ia", length = 50)
    private String modeloIa;

    /** Versión del plan (para control de cambios) */
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    /** Indica si el plan está activo */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /** Establece la fecha de generación antes de persistir */
    @PrePersist
    protected void onCreate() {
        fechaGeneracion = LocalDateTime.now();
    }
}