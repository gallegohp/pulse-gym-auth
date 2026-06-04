package com.pulse_gym.lb_common.entity.user;

import java.math.BigDecimal;

import com.pulse_gym.lb_common.enums.EnumTipoDuracion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "membresias")
@Data
public class Membresia {
    
    /** El ID de la membresía */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membresia")
    private Long idMembresia;
    
    /** El nombre de la membresía */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    /** El precio total de la membresía */
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;
    
    /** La duración en meses */
    @Column(name = "duracion_meses")
    private Integer duracionMeses = 1;
    
    /** El tipo de duración */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_duracion", nullable = false)
    private EnumTipoDuracion tipoDuracion;
    
    /** Indica si la membresía incluye IA */
    @Column(name = "incluye_ia", nullable = false)
    private Boolean incluyeIA;
    
    /** Indica si la membresía es flexible */
    @Column(name = "es_flexible", nullable = false)
    private Boolean esFlexible;
    
    /** El precio por día */
    @Column(name = "precio_por_dia", precision = 10, scale = 2)
    private BigDecimal precioPorDia;
    
    /** Indica si la membresía está activa */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}