package com.pulse_gym.ms_operation.services;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.MantenimientoRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.operation.Equipo;
import com.pulse_gym.lb_common.entity.operation.Mantenimiento;
import com.pulse_gym.lb_common.entity.operation.Proveedor;
import com.pulse_gym.lb_common.enums.EnumTipoMantenimiento;
import com.pulse_gym.ms_operation.repository.EquipoRepository;
import com.pulse_gym.ms_operation.repository.MantenimientoRepository;
import com.pulse_gym.ms_operation.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class MantenimientoService {

    private final ProveedorRepository proveedorRepository;

    private final EquipoRepository equipoRepository;

    private final MantenimientoRepository mantenimientoRepository;

    public MessegeGlobalDTO registrarMantenimiento(MantenimientoRequestDTO mantenimientoRequestDTO) {

        Proveedor proveedor = null;
        if (mantenimientoRequestDTO.getIdProveedor() != null) {
            proveedor = proveedorRepository
                    .findById(mantenimientoRequestDTO.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        }

        Equipo equipo = equipoRepository
                .findById(mantenimientoRequestDTO.getIdEquipo())
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        EnumTipoMantenimiento tipoMantenimiento;

        try {
            tipoMantenimiento = EnumTipoMantenimiento.valueOf(mantenimientoRequestDTO.getTipo().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de mantenimiento no válido. Debe ser PREVENTIVO o CORRECTIVO");
        }

        Mantenimiento mantenimiento = new Mantenimiento();

        mantenimiento.setProveedor(proveedor);
        mantenimiento.setEquipo(equipo);

        mantenimiento.setFechaServicio(mantenimientoRequestDTO.getFechaServicio());
        mantenimiento.setTipo(tipoMantenimiento);
        mantenimiento.setDescripcion(mantenimientoRequestDTO.getDescripcion());
        mantenimiento.setCosto(mantenimientoRequestDTO.getCosto());
        mantenimiento.setProximoMantenimiento(mantenimientoRequestDTO.getProximoMantenimiento());
        mantenimiento.setTecnicoResponsable(mantenimientoRequestDTO.getTecnicoResponsable());
        mantenimiento.setProximoMantenimiento(mantenimientoRequestDTO.getProximoMantenimiento());

        mantenimientoRepository.save(mantenimiento);

        return new MessegeGlobalDTO("Mantenimiento registrado exitosamente");
    }

}
