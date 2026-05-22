package com.pulse_gym.lb_common.dto;

import java.time.LocalDate;

import com.pulse_gym.lb_common.enums.EnumEstado;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EquipoRequestDTO {
    
    @NotNull(message = "El id del proveedor es obligatorio")
    private Long idProveedor;

    @NotNull(message = "El id de la sede es obligatorio")
    private Long idSede;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @Size(max = 50, message = "La marca no puede superar 50 caracteres")
    private String marca;

    @Size(max = 50, message = "El modelo no puede superar 50 caracteres")
    private String modelo;

    @Size(max = 100, message = "El número de serie no puede superar 100 caracteres")
    private String numeroSerie;

    private LocalDate fechaAdquisicion;

    private LocalDate fechaGarantia;

    @Size(max = 100, message = "La ubicación no puede superar 100 caracteres")
    private String ubicacion;

    @NotNull(message = "El estado es obligatorio")
    private EnumEstado estado;
}
