package com.pulse_gym.ms_operation.services;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.ConsultaEquipoRequestDTO;
import com.pulse_gym.lb_common.dto.EquipoRequestDTO;
import com.pulse_gym.lb_common.dto.EquipoResponseDTO;
import com.pulse_gym.lb_common.dto.HttpGlobalResponse;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.operation.Equipo;
import com.pulse_gym.lb_common.entity.operation.Proveedor;
import com.pulse_gym.lb_common.entity.operation.Sede;
import com.pulse_gym.lb_common.enums.EnumEstado;
import com.pulse_gym.ms_operation.repository.EquipoRepository;
import com.pulse_gym.ms_operation.repository.ProveedorRepository;
import com.pulse_gym.ms_operation.repository.SedeRepository;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipoService {

        /**
         * Inyeccion de EquipoRepository
         */
        private final EquipoRepository equipoRepository;

        /**
         * Inyeccion de ProveedorRepository para manejar las operaciones de base de
         * datos relacionadas con los proveedores
         */
        private final ProveedorRepository proveedorRepository;

        /**
         * Inyeccion de SedeRepository para manejar las operaciones de base de datos
         * relacionadas con las sedes
         */
        private final SedeRepository sedeRepository;

        /**
         * Registra un nuevo equipo en el sistema. Primero verifica que el número de
         * serie del equipo no exista ya en la base de datos
         * 
         * @param equipoRequestDTO
         * @return MessegeGlobalDTO con un mensaje de éxito si el equipo se registró
         *         correctamente
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

        /**
         * Obtiene una lista de equipos que coinciden con los criterios de búsqueda
         * especificados en el objeto ConsultaEquipoRequestDTO.
         * Specification tiene la funcion de construir dinámicamente consultas basadas
         * en los campos proporcionados en el DTO, lo que permite una búsqueda
         * flexible y eficiente en la base de datos.
         * 
         * @param request
         * @return
         */
        public List<Equipo> obtenerEquipos(ConsultaEquipoRequestDTO request) {
                Specification<Equipo> spec = buildSpecification(request);
                return equipoRepository.findAll(spec);
        }

        private Specification<Equipo> buildSpecification(ConsultaEquipoRequestDTO request) {
                return (root, query, cb) -> {
                        // Especifica el tipo jakarta.persistence.criteria.Predicate
                        List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

                        // Búsqueda por nombre
                        if (StringUtils.isNotBlank(request.getNombre())) {
                                predicates.add(cb.like(cb.lower(root.get("nombre")),
                                                "%" + request.getNombre().toLowerCase() + "%"));
                        }

                        // Búsqueda por marca
                        if (StringUtils.isNotBlank(request.getMarca())) {
                                predicates.add(cb.like(cb.lower(root.get("marca")),
                                                "%" + request.getMarca().toLowerCase() + "%"));
                        }

                        // Búsqueda por ubicación
                        if (StringUtils.isNotBlank(request.getUbicacion())) {
                                predicates.add(cb.like(cb.lower(root.get("ubicacion")),
                                                "%" + request.getUbicacion().toLowerCase() + "%"));
                        }

                        // Búsqueda por estado
                        if (StringUtils.isNotBlank(request.getEstado())) {
                                try {
                                        EnumEstado estadoEnum = EnumEstado.valueOf(request.getEstado().toUpperCase());
                                        predicates.add(cb.equal(root.get("estado"), estadoEnum));
                                } catch (IllegalArgumentException e) {
                                        // Estado no válido
                                }
                        }

                        // Búsqueda por sede
                        if (request.getIdSede() != null) {
                                predicates.add(cb.equal(root.get("sede").get("idSede"), request.getIdSede()));
                        }

                        return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
                };
        }

}