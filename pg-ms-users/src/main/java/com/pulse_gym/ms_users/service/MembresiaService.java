package com.pulse_gym.ms_users.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pulse_gym.lb_common.dto.MembresiaRequestDTO;
import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.entity.user.Membresia;
import com.pulse_gym.lb_common.enums.EnumTipoDuracion;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.repository.MembresiaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembresiaService {

    /** El repositorio de membresías */
    private final MembresiaRepository membresiaRepository;

    /**
     * Crea una nueva membresía
     * 
     * @param requestDTO Los datos para crear la membresía
     * @param userRol    El rol del usuario que realiza la acción
     * @return Un mensaje global con la información de la membresía creada
     */
    @Transactional
    public MessegeGlobalDTO crearMembresia(MembresiaRequestDTO requestDTO, String userRol) {
        ValidacionDeRoles.validarAdminORecepcionista(userRol);

        if (membresiaRepository.existsByNombreAndActivoTrue(requestDTO.getNombre())) {
            throw new RuntimeException("Ya existe una membresía activa con el nombre: " + requestDTO.getNombre());
        }

        EnumTipoDuracion tipoDuracion;
        try {
            tipoDuracion = EnumTipoDuracion.valueOf(requestDTO.getTipoDuracion().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Tipo de duración no válido. Valores: DIA, SEMANA, MES, TRIMESTRE, SEMESTRE, ANUAL");
        }

        if (requestDTO.getPrecioPorDia() == null || requestDTO.getPrecioPorDia().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El precio por día es obligatorio y debe ser mayor a 0");
        }

        Membresia membresia = new Membresia();
        membresia.setNombre(requestDTO.getNombre());
        membresia.setCantidad(requestDTO.getCantidad() != null ? requestDTO.getCantidad() : 1);
        membresia.setTipoDuracion(tipoDuracion);
        membresia.setIncluyeIA(requestDTO.getIncluyeIA());
        membresia.setEsFlexible(requestDTO.getEsFlexible());
        membresia.setPrecioPorDia(requestDTO.getPrecioPorDia());
        membresia.setBeneficios(requestDTO.getBeneficios());
        membresia.setRestricciones(requestDTO.getRestricciones());
        membresia.setActivo(requestDTO.getActivo() != null ? requestDTO.getActivo() : true);

        BigDecimal precioTotalCalculado = membresia.calcularPrecioTotal();
        membresia.setPrecioTotal(precioTotalCalculado);

        membresiaRepository.save(membresia);

        String tipoMembresia = requestDTO.getEsFlexible() ? "Flexible " : "";
        String iaTexto = requestDTO.getIncluyeIA() ? "con IA" : "sin IA";

        return new MessegeGlobalDTO(
                String.format("Membresía %s%s %s creada correctamente. Duración: %s, Precio total: $%,.0f",
                        tipoMembresia, requestDTO.getNombre(), iaTexto,
                        membresia.getDuracionDescripcion(), precioTotalCalculado));
    }
}
