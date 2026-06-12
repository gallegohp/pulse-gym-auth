package com.pulse_gym.ms_users.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.AsignarMembresiaRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.RenovarMembresiaRequestDTO;
import com.pulse_gym.lb_common.dto.SocioMembresiaResponseDTO;
import com.pulse_gym.lb_common.entity.user.Membresia;
import com.pulse_gym.lb_common.entity.user.SocioMembresia;
import com.pulse_gym.lb_common.entity.user.UsuarioPerfil;
import com.pulse_gym.lb_common.enums.EnumEstadoSocioMembresia;
import com.pulse_gym.lb_common.enums.EnumRol;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
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
     * Calcula la fecha de vencimiento de una membresía a partir de la fecha de
     * inicio y el tipo de duración de la membresía.
     * 
     * @param fechaInicio La fecha de inicio de la membresía
     * @param membresia   La membresía para la cual se va a calcular la fecha de
     *                    vencimiento
     * @return La fecha de vencimiento calculada para la membresía del socio
     */
    private LocalDate calcularFechaVencimiento(LocalDate fechaInicio, Membresia membresia) {
        int diasTotales = membresia.getTipoDuracion().calcularDiasTotales(
                membresia.getCantidad() != null ? membresia.getCantidad() : 1);
        return fechaInicio.plusDays(diasTotales);
    }

    /**
     * Convierte una entidad SocioMembresia a un DTO de respuesta
     * SocioMembresiaResponseDTO.
     * 
     * @param sm La entidad SocioMembresia a convertir
     * @return Un objeto SocioMembresiaResponseDTO con los datos de la membresía del
     *         socio
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

    /**
     * Asigna una membresía a un socio.
     * 
     * @param requestDTO Datos de la asignación (idSocio, idMembresia, fechaInicio,
     *                   renovacionAutomatica, observaciones)
     * @param userRol    Rol del usuario que hace la solicitud
     * @return Mensaje con el formato: "Membresía 'X' asignada correctamente al
     *         socio Y. Vence el: Z"
     */
    @Transactional
    public MessegeGlobalDTO asignarMembresia(AsignarMembresiaRequestDTO requestDTO, String userRol) {
        ValidacionDeRoles.validarRecepcionista(userRol);

        UsuarioPerfil socio = usuarioRepository.findById(requestDTO.getIdSocio())
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + requestDTO.getIdSocio()));

        Membresia membresia = membresiaRepository.findById(requestDTO.getIdMembresia())
                .orElseThrow(
                        () -> new RuntimeException("Membresía no encontrada con ID: " + requestDTO.getIdMembresia()));

        if (!membresia.getActivo()) {
            throw new RuntimeException("La membresía no está activa");
        }

        if (socioMembresiaRepository.existsBySocio_IdUsuarioAndEstado(requestDTO.getIdSocio(),
                EnumEstadoSocioMembresia.ACTIVA)) {
            throw new RuntimeException(
                    "El socio ya tiene una membresía activa. Debe renovar o cancelar la actual primero.");
        }

        LocalDate fechaInicio = requestDTO.getFechaInicio() != null ? requestDTO.getFechaInicio() : LocalDate.now();
        LocalDate fechaVencimiento = calcularFechaVencimiento(fechaInicio, membresia);

        SocioMembresia socioMembresia = new SocioMembresia();
        socioMembresia.setSocio(socio);
        socioMembresia.setMembresia(membresia);
        socioMembresia.setFechaInicio(fechaInicio);
        socioMembresia.setFechaVencimiento(fechaVencimiento);
        socioMembresia.setEstado(EnumEstadoSocioMembresia.ACTIVA);
        socioMembresia.setRenovacionAutomatica(
                requestDTO.getRenovacionAutomatica() != null ? requestDTO.getRenovacionAutomatica() : false);
        socioMembresia.setObservaciones(requestDTO.getObservaciones());

        socioMembresiaRepository.save(socioMembresia);

        return new MessegeGlobalDTO(String.format(
                "Membresía '%s' asignada correctamente al socio %s. Vence el: %s",
                membresia.getNombre(), socio.getNombre(), fechaVencimiento));
    }

    /**
     * Consulta todas las membresías de un socio.
     * 
     * @param idSocio           ID del socio a consultar
     * @param userRol           Rol del usuario autenticado (socio, administrador o
     *                          recepcionista)
     * @param userIdAutenticado ID del usuario que realiza la consulta
     * @return Lista de DTOs con los datos de cada membresía del socio (incluye
     *         fechas, estado, renovación automática, etc.)
     */
    @Transactional(readOnly = true)
    public List<SocioMembresiaResponseDTO> consultarMembresiasSocio(Long idSocio, String userRol,
            Long userIdAutenticado) {

        if (userRol.equals(EnumRol.socio.name())) {
            if (!userIdAutenticado.equals(idSocio)) {
                throw new SecurityAuthorizationException("Acceso denegado. Solo puede consultar su propia membresía");
            }
        } else if (!userRol.equals(EnumRol.administrador.name()) && !userRol.equals(EnumRol.recepcionista.name())) {
            throw new SecurityAuthorizationException("Acceso denegado. Rol no autorizado: " + userRol);
        }

        UsuarioPerfil socio = usuarioRepository.findById(idSocio)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado con ID: " + idSocio));

        List<SocioMembresia> membresias = socioMembresiaRepository
                .findBySocio_IdUsuarioOrderByFechaCreacionDesc(idSocio);

        if (membresias.isEmpty()) {
            throw new RuntimeException("El socio " + socio.getNombre() + " no tiene membresías asignadas");
        }

        return membresias.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Renueva una membresía existente de un socio. Marca la membresía actual como
     * RENOVADA y crea una nueva asignación con fechas actualizadas.
     * 
     * @param requestDTO        DTO con el idSocioMembresia de la membresía a
     *                          renovar, más opciones de renovación automática y
     *                          observaciones
     * @param userRol           Rol del usuario autenticado (socio, administrador o
     *                          recepcionista)
     * @param userIdAutenticado ID del usuario que realiza la solicitud
     * @return Mensaje de confirmación con la nueva fecha de vencimiento
     */
    @Transactional
    public MessegeGlobalDTO renovarMembresia(RenovarMembresiaRequestDTO requestDTO, String userRol,
            Long userIdAutenticado) {

        SocioMembresia socioMembresia = socioMembresiaRepository.findById(requestDTO.getIdSocioMembresia())
                .orElseThrow(() -> new RuntimeException(
                        "Asignación de membresía no encontrada con ID: " + requestDTO.getIdSocioMembresia()));

        if (userRol.equals(EnumRol.socio.name())) {
            if (!userIdAutenticado.equals(socioMembresia.getSocio().getIdUsuario())) {
                throw new SecurityAuthorizationException("Acceso denegado. Solo puede renovar su propia membresía");
            }
        } else if (!userRol.equals(EnumRol.administrador.name()) && !userRol.equals(EnumRol.recepcionista.name())) {
            throw new SecurityAuthorizationException("Acceso denegado. Rol no autorizado: " + userRol);
        }

        Membresia membresia = socioMembresia.getMembresia();
        if (!membresia.getActivo()) {
            throw new RuntimeException("La membresía base ya no está activa");
        }

        socioMembresia.setEstado(EnumEstadoSocioMembresia.RENOVADA);
        socioMembresiaRepository.save(socioMembresia);

        LocalDate nuevaFechaInicio = LocalDate.now();
        LocalDate nuevaFechaVencimiento = calcularFechaVencimiento(nuevaFechaInicio, membresia);

        SocioMembresia nuevaMembresia = new SocioMembresia();
        nuevaMembresia.setSocio(socioMembresia.getSocio());
        nuevaMembresia.setMembresia(membresia);
        nuevaMembresia.setFechaInicio(nuevaFechaInicio);
        nuevaMembresia.setFechaVencimiento(nuevaFechaVencimiento);
        nuevaMembresia.setEstado(EnumEstadoSocioMembresia.ACTIVA);
        nuevaMembresia.setRenovacionAutomatica(
                requestDTO.getRenovacionAutomatica() != null ? requestDTO.getRenovacionAutomatica()
                        : socioMembresia.getRenovacionAutomatica());
        nuevaMembresia.setObservaciones("Renovación de membresía anterior ID: " + socioMembresia.getIdSocioMembresia() +
                (requestDTO.getObservaciones() != null ? " - " + requestDTO.getObservaciones() : ""));

        socioMembresiaRepository.save(nuevaMembresia);

        return new MessegeGlobalDTO(String.format(
                "Membresía renovada correctamente. Nueva fecha de vencimiento: %s",
                nuevaFechaVencimiento));
    }
}
