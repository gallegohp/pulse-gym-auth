// SedeService.java
package com.pulse_gym.ms_operation.services;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.SedeRequestDTO;
import com.pulse_gym.lb_common.dto.SedeResponseDTO;
import com.pulse_gym.lb_common.dto.SedeUpdateDTO;
import com.pulse_gym.lb_common.entity.operation.Sede;
import com.pulse_gym.ms_operation.repository.SedeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SedeService {

    private final SedeRepository sedeRepository;

    @Transactional
    public MessegeGlobalDTO crearSede(SedeRequestDTO request) {
        if (sedeRepository.existsByNombreSede(request.getNombreSede())) {
            throw new RuntimeException("Ya existe una sede con el nombre: " + request.getNombreSede());
        }

        Sede sede = new Sede();
        sede.setNombreSede(request.getNombreSede());
        sede.setDireccion(request.getDireccion());
        sede.setTelefono(request.getTelefono());
        sede.setCiudad(request.getCiudad());

        Sede saved = sedeRepository.save(sede);
        
        return new MessegeGlobalDTO("Sede creada exitosamente con ID: " + saved.getIdSede());
    }

    public List<SedeResponseDTO> obtenerTodasLasSedes() {
        List<Sede> sedes = sedeRepository.findAll(Sort.by(Sort.Direction.ASC, "nombreSede"));
        
        if (sedes.isEmpty()) {
            throw new RuntimeException("No hay sedes registradas");
        }
        
        return sedes.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public SedeResponseDTO obtenerSedePorId(Long id) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + id));
        
        return convertirAResponseDTO(sede);
    }

    @Transactional
    public MessegeGlobalDTO actualizarSede(Long id, SedeUpdateDTO request) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + id));

        if (request.getNombreSede() != null && !request.getNombreSede().equals(sede.getNombreSede())) {
            if (sedeRepository.existsByNombreSede(request.getNombreSede())) {
                throw new RuntimeException("Ya existe otra sede con el nombre: " + request.getNombreSede());
            }
            sede.setNombreSede(request.getNombreSede());
        }

        if (request.getDireccion() != null) {
            sede.setDireccion(request.getDireccion());
        }
        if (request.getTelefono() != null) {
            sede.setTelefono(request.getTelefono());
        }
        if (request.getCiudad() != null) {
            sede.setCiudad(request.getCiudad());
        }

        sedeRepository.save(sede);
        
        return new MessegeGlobalDTO("Sede actualizada exitosamente");
    }

    @Transactional
    public MessegeGlobalDTO eliminarSede(Long id) {
        Sede sede = sedeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada con ID: " + id));

        Long cantidadEquipos = sedeRepository.countEquiposBySedeId(id);
        if (cantidadEquipos != null && cantidadEquipos > 0) {
            throw new RuntimeException("No se puede eliminar la sede porque tiene " + cantidadEquipos + " equipos asociados");
        }

        sedeRepository.delete(sede);
        
        return new MessegeGlobalDTO("Sede eliminada exitosamente");
    }

    public List<SedeResponseDTO> buscarSedesPorNombre(String nombre) {
        List<Sede> sedes = sedeRepository.findByNombreSedeContainingIgnoreCase(nombre);
        
        if (sedes.isEmpty()) {
            throw new RuntimeException("No se encontraron sedes con el nombre: " + nombre);
        }
        
        return sedes.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public List<SedeResponseDTO> buscarSedesPorCiudad(String ciudad) {
        List<Sede> sedes = sedeRepository.findByCiudadContainingIgnoreCase(ciudad);
        
        if (sedes.isEmpty()) {
            throw new RuntimeException("No se encontraron sedes en la ciudad: " + ciudad);
        }
        
        return sedes.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    public Page<SedeResponseDTO> obtenerSedesPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombreSede").ascending());
        Page<Sede> sedesPage = sedeRepository.findAll(pageable);
        
        if (sedesPage.isEmpty() && page == 0) {
            throw new RuntimeException("No hay sedes registradas");
        }
        
        return sedesPage.map(this::convertirAResponseDTO);
    }

    private SedeResponseDTO convertirAResponseDTO(Sede sede) {
        SedeResponseDTO dto = new SedeResponseDTO();
        dto.setIdSede(sede.getIdSede());
        dto.setNombreSede(sede.getNombreSede());
        dto.setDireccion(sede.getDireccion());
        dto.setTelefono(sede.getTelefono());
        dto.setCiudad(sede.getCiudad());
        
        Long cantidadEquipos = sedeRepository.countEquiposBySedeId(sede.getIdSede());
        dto.setCantidadEquipos(cantidadEquipos != null ? cantidadEquipos.intValue() : 0);
        
        return dto;
    }
}