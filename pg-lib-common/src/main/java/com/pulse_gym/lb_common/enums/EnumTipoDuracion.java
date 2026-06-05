package com.pulse_gym.lb_common.enums;

public enum EnumTipoDuracion {
    DIA("días", 1),
    SEMANA("semanas", 7),
    MES("meses", 30),
    TRIMESTRE("trimestres", 90),
    SEMESTRE("semestres", 180),
    ANUAL("años", 365);

    private final String nombre;
    private final int diasPorUnidad;

    EnumTipoDuracion(String nombre, int diasPorUnidad) {
        this.nombre = nombre;
        this.diasPorUnidad = diasPorUnidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getDiasPorUnidad() {
        return diasPorUnidad;
    }

    /**
     * Calcula los días totales basado en la cantidad
     * 
     * @param cantidad Número de unidades (ej: 3 semanas, 2 meses)
     * @return Total de días
     */
    public int calcularDiasTotales(int cantidad) {
        return diasPorUnidad * cantidad;
    }
}