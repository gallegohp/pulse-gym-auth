package com.pulse_gym.ms_users.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.SocioMembresiaResponseDTO;
import com.pulse_gym.lb_common.entity.user.Membresia;
import com.pulse_gym.lb_common.entity.user.SocioMembresia;
import com.pulse_gym.ms_users.repository.MembresiaRepository;
import com.pulse_gym.ms_users.repository.SocioMembresiaRepository;
import com.pulse_gym.ms_users.repository.UsuarioPerfilRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocioMembresiaService {

    /** Repositorio para gestionar las membresías de los socios */
    private final SocioMembresiaRepository socioMembresiaRepository;

    /** Repositorio para gestionar los perfiles de usuario */
    private final UsuarioPerfilRepository usuarioRepository;

    /** Repositorio para gestionar las membresías */
    private final MembresiaRepository membresiaRepository; 

    /**
     * Calcula la fecha de vencimiento de una membresía a partir de la fecha de inicio y el tipo de duración de la membresía.
     * @param fechaInicio La fecha de inicio de la membresía
     * @param membresia La membresía para la cual se va a calcular la fecha de vencimiento
     * @return La fecha de vencimiento calculada para la membresía del socio
     */
     private LocalDate calcularFechaVencimiento(LocalDate fechaInicio, Membresia membresia) {
        int diasTotales = membresia.getTipoDuracion().calcularDiasTotales(
            membresia.getCantidad() != null ? membresia.getCantidad() : 1);
        return fechaInicio.plusDays(diasTotales);
    }
    
    /**
     * Convierte una entidad SocioMembresia a un DTO de respuesta SocioMembresiaResponseDTO.
     * @param sm La entidad SocioMembresia a convertir
     * @return Un objeto SocioMembresiaResponseDTO con los datos de la membresía del socio
     */
    private SocioMembresiaResponseDTO convertirAResponseDTO(SocioMembresia sm) {
        SocioMembresiaResponseDTO dto = new SocioMembresiaResponseDTO();
        dto.setIdSocioMembresia(sm.getIdSocioMembresia());
        dto.setIdSocio(sm.getSocio().getIdUsuario());
        dto.setNombreSocio(sm.getSocio().getNombre() + " " + sm.getSocio().getApellido());
        dto.setEmailSocio(sm.getSocio().getEmail());
        dto.setIdMembresia(sm.getMembresia().getIdMembresia());
        dto.setNombreMembresia(sm.getMembresia().getNombre());
        dto.setPrecioTotal(sm.getMembresia().getPrecioTotal());
        dto.setCantidad(sm.getMembresia().getCantidad());
        dto.setTipoDuracion(sm.getMembresia().getTipoDuracion().name());
        dto.setDuracionDescripcion(sm.getMembresia().getDuracionDescripcion());
        dto.setIncluyeIA(sm.getMembresia().getIncluyeIA());
        dto.setFechaInicio(sm.getFechaInicio());
        dto.setFechaVencimiento(sm.getFechaVencimiento());
        dto.setEstado(sm.getEstado().name());
        dto.setRenovacionAutomatica(sm.getRenovacionAutomatica());
        dto.setDiasRestantes(sm.getDiasRestantes());
        dto.setEstaActiva(sm.isActiva());
        dto.setEstaVencida(sm.isVencida());
        dto.setBeneficios(sm.getMembresia().getBeneficios());
        dto.setRestricciones(sm.getMembresia().getRestricciones());
        dto.setFechaCreacion(sm.getFechaCreacion());
        dto.setFechaActualizacion(sm.getFechaActualizacion());
        return dto;
    }
    
}
