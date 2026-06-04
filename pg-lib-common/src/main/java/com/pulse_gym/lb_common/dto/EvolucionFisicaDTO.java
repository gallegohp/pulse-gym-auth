package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class EvolucionFisicaDTO {
    private Long idSocio;
    private String nombreSocio;
    private List<PuntoEvolucion> evolucionPeso;
    private List<PuntoEvolucion> evolucionGrasa;
    private List<PuntoEvolucion> evolucionMusculo;
    
    @Data
    public static class PuntoEvolucion {
        private LocalDateTime fecha;
        private BigDecimal valor;
        
        public PuntoEvolucion(LocalDateTime fecha, BigDecimal valor) {
            this.fecha = fecha;
            this.valor = valor;
        }
    }
}