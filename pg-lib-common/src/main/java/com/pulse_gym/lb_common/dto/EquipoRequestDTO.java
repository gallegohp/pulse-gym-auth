package com.pulse_gym.lb_common.dto;

import java.time.LocalDate;

import com.pulse_gym.lb_common.enums.EnumEstado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EquipoRequestDTO {
    
    /**
     * id del proveedor del equipo
     */
    @NotNull(message = "El id del proveedor es obligatorio")
    private Long idProveedor;

    /**
     * id de la sede a la que pertenece el equipo
     */
    @NotNull(message = "El id de la sede es obligatorio")
    private Long idSede;

    /**
     * nombre del equipo
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    /**
     * descripcion del equipo
     */
    @Size(max = 50, message = "La marca no puede superar 50 caracteres")
    private String marca;

    /**
     * descripcion del equipo
     */
    @Size(max = 50, message = "El modelo no puede superar 50 caracteres")
    private String modelo;

    /**
     * número de serie del equipo
     */
    @Size(max = 100, message = "El número de serie no puede superar 100 caracteres")
    private String numeroSerie;

    /**
     * fecha de adquisición del equipo
     */
    private LocalDate fechaAdquisicion;

    /**
     * fecha de garantía del equipo
     */
    private LocalDate fechaGarantia;

    /**
     * ubicación del equipo dentro de la sede
     */
    @Size(max = 100, message = "La ubicación no puede superar 100 caracteres")
    private String ubicacion;

    /**
     * estado del equipo (activo, en mantenimiento, fuera de servicio)
     */
    @NotNull(message = "El estado es obligatorio")
    private EnumEstado estado;
}
