package com.pulse_gym.lb_common.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class EvolucionFisicaDTO {

    /** ID del socio */
    private Long idSocio;

    /** Nombre del socio */
    private String nombreSocio;

    /** Lista de puntos de evolución del peso */
    private List<PuntoEvolucion> evolucionPeso;

    /** Lista de puntos de evolución de la grasa */
    private List<PuntoEvolucion> evolucionGrasa;

    /** Lista de puntos de evolución del músculo */
    private List<PuntoEvolucion> evolucionMusculo;

    /** Punto de evolución */
    @Data

    /** Punto de evolución */
    public static class PuntoEvolucion {

        /** Fecha de la medición */
        private LocalDateTime fecha;

        /** Valor de la medición */
        private BigDecimal valor;

        /**
         * Constructor de la clase PuntoEvolucion
         * 
         * @param fecha Fecha de la medición
         * @param valor Valor de la medición
         */
        public PuntoEvolucion(LocalDateTime fecha, BigDecimal valor) {
            this.fecha = fecha;
            this.valor = valor;
        }
    }
}