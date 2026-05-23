package com.pulse_gym.ms_operation.services;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.EquipoRequestDTO;
import com.pulse_gym.lb_common.dto.EquipoResponseDTO;
import com.pulse_gym.lb_common.dto.HttpGlobalResponse;
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

        /**
         * Inyeccion de EquipoRepository
         */
        private final EquipoRepository equipoRepository;

        /**
         * Inyeccion de ProveedorRepository para manejar las operaciones de base de datos relacionadas con los proveedores
         */
        private final ProveedorRepository proveedorRepository;

        /**
         * Inyeccion de SedeRepository para manejar las operaciones de base de datos relacionadas con las sedes
         */
        private final SedeRepository sedeRepository;

        /**
         * Registra un nuevo equipo en el sistema. Primero verifica que el número de serie del equipo no exista ya en la base de datos
         * @param equipoRequestDTO
         * @return MessegeGlobalDTO con un mensaje de éxito si el equipo se registró correctamente
         */
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
                equipo.setFechaAdquisicion(equipoRequestDTO.getFechaAdquisicion());
                equipo.setFechaGarantia(equipoRequestDTO.getFechaGarantia());
                equipo.setUbicacion(equipoRequestDTO.getUbicacion());
                equipo.setEstado(equipoRequestDTO.getEstado());

                equipoRepository.save(equipo);

                MessegeGlobalDTO response = new MessegeGlobalDTO("Equipo registrado correctamente");
                return response;
        }

        public HttpGlobalResponse<EquipoResponseDTO> obtenerEquipos() {
                HttpGlobalResponse<EquipoResponseDTO> response = new HttpGlobalResponse<>();

                response.setData(equipoRepository.findAll().stream().map(equipo -> {
                        EquipoResponseDTO dto = new EquipoResponseDTO();

                        dto.setIdEquipo(equipo.getIdEquipo());
                        dto.setNombreEquipo(equipo.getNombre());
                        dto.setDescripcion(equipo.getMarca() + " " + equipo.getModelo());
                        dto.setEstado(equipo.getEstado());
                        dto.setIdSede(equipo.getSede().idSede());
                        dto.setIdProveedor(equipo.getProveedor().getId());

                        return dto;
                }).toList());

                response.setMessage("Equipos obtenidos correctamente");
                return response;
        }
}