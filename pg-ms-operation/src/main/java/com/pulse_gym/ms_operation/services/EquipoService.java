package com.pulse_gym.ms_operation.services;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.EquipoRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.operation.Equipo;
import com.pulse_gym.lb_common.entity.operation.Proveedor;
import com.pulse_gym.lb_common.entity.operation.Sede;
import com.pulse_gym.ms_operation.repository.EquipoRepository;
import com.pulse_gym.ms_operation.repository.ProveedorRepository;
import com.pulse_gym.ms_operation.repository.SedeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipoService {

        private final EquipoRepository equipoRepository;
        private final ProveedorRepository proveedorRepository;
        private final SedeRepository sedeRepository;


        public MessegeGlobalDTO registrarEquipo(EquipoRequestDTO equipoRequestDTO) {

                if (equipoRepository
                                .findByNumeroSerie(equipoRequestDTO.getNumeroSerie())
                                .isPresent()) {

                        MessegeGlobalDTO response = new MessegeGlobalDTO("El número de serie ya existe");
                        return response;
                }

                Proveedor proveedor = proveedorRepository
                                .findById(equipoRequestDTO.getIdProveedor())
                                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

                Sede sede = sedeRepository
                                .findById(equipoRequestDTO.getIdSede())
                                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

                Equipo equipo = new Equipo();

                equipo.setProveedor(proveedor);
                equipo.setSede(sede);

                equipo.setNombre(equipoRequestDTO.getNombre());
                equipo.setMarca(equipoRequestDTO.getMarca());
                equipo.setModelo(equipoRequestDTO.getModelo());
                equipo.setNumeroSerie(equipoRequestDTO.getNumeroSerie());

                equipo.setFechaAdquisicion(
                                equipoRequestDTO.getFechaAdquisicion());

                equipo.setFechaGarantia(
                                equipoRequestDTO.getFechaGarantia());

                equipo.setUbicacion(
                                equipoRequestDTO.getUbicacion());

                equipo.setEstado(
                                equipoRequestDTO.getEstado());

                equipoRepository.save(equipo);

                MessegeGlobalDTO response = new MessegeGlobalDTO("Equipo registrado correctamente");
                return response;
        }
}