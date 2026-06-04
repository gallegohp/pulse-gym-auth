package com.pulse_gym.lb_common.entity.user;

import java.math.BigDecimal;

import com.pulse_gym.lb_common.enums.TipoDuracion;

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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membresia")
    private Long idMembresia;
    
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;
    
    @Column(name = "duracion_meses")
    private Integer duracionMeses = 1;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_duracion", nullable = false)
    private TipoDuracion tipoDuracion;
    
    @Column(name = "incluye_ia", nullable = false)
    private Boolean incluyeIA;
    
    @Column(name = "es_flexible", nullable = false)
    private Boolean esFlexible;
    
    @Column(name = "precio_por_dia", precision = 10, scale = 2)
    private BigDecimal precioPorDia;
    
    @Column(name = "beneficios", columnDefinition = "TEXT")
    private String beneficios;
    
    @Column(name = "restricciones", columnDefinition = "TEXT")
    private String restricciones;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}