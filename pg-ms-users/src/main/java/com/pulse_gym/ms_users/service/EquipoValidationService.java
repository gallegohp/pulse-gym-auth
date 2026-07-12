package com.pulse_gym.ms_users.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.client.EquipoClient;
import com.pulse_gym.lb_common.dto.ConsultaEquipoRequestDTO;
import com.pulse_gym.lb_common.entity.operation.Equipo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EquipoValidationService {
    
    /** Cliente Feign para consultar equipos desde pg-ms-operation */
    private final EquipoClient equipoClient;

    /**
     * Valida que un equipo exista en pg-ms-operation por su nombre
     * 
     * @param nombreEquipo Nombre del equipo a validar
     * @return true si el equipo existe y está operativo, false en caso contrario
     */

    public boolean validarEquipoExistente(String nombreEquipo) {
        try {
            ConsultaEquipoRequestDTO request = new ConsultaEquipoRequestDTO();
            request.setNombre(nombreEquipo);

            List<Equipo> equipos = equipoClient.consultarEquipos(request);

            if (equipos == null || equipos.isEmpty()) {
                log.warn("Equipo no encontrado: {}", nombreEquipo);
                return false;
            }

            // Verificar que al menos uno esté OPERATIVO
            boolean existeOperativo = equipos.stream()
                    .anyMatch(e -> e.getEstado() != null &&
                            e.getEstado().name().equals("OPERATIVO"));

            if (!existeOperativo) {
                log.warn("El equipo '{}' existe pero no está en estado OPERATIVO", nombreEquipo);
                return false;
            }

            log.info("Equipo validado exitosamente: {}", nombreEquipo);
            return true;

        } catch (Exception e) {
            log.error("Error al validar equipo '{}': {}", nombreEquipo, e.getMessage());
            return false;
        }
    }
}
