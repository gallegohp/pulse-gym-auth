package com.pulse_gym.ms_notifications.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.PlantillaNotificacionRequestDTO;
import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;
import com.pulse_gym.lb_common.enums.EnumTipoPlantilla;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_notifications.repository.PlantillaNotificationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class PlantillaService {

    private final PlantillaNotificationRepository plantillaNotificationRepository;

    @Transactional
    public MessegeGlobalDTO crearPlantilla(PlantillaNotificacionRequestDTO request, String userRol) {
        ValidacionDeRoles.validarAdmin(userRol);

        EnumTipoPlantilla tipoPlantilla;

        try {
            tipoPlantilla = EnumTipoPlantilla.valueOf(request.getTipoPlantilla().toUpperCase());
        }catch (IllegalArgumentException e) {
            throw new RuntimeException("Tipo de plantilla no válido. Debe ser 'EMAIL' o 'WHATSAPP'");
        }

        EnumEventoAsociado eventoAsociado;

        try {
            eventoAsociado = EnumEventoAsociado.valueOf(request.getEventoAsociado().toUpperCase());
        }catch (IllegalArgumentException e) {
            throw new RuntimeException("Evento asociado no válido. Debe ser 'WELCOME', 'PAYMENT_REMINDER', 'ACHIEVEMENT' o 'MAINTENANCE_ALERT'");
        }

        PlantillaNotificacion notificacion = new PlantillaNotificacion();

        notificacion.setNombre(request.getNombre());
        notificacion.setDescripcion(request.getDescripcion());
        notificacion.setContenido(request.getContenido());
        notificacion.setTipoPlantilla(tipoPlantilla);
        notificacion.setEventoAsociado(eventoAsociado);
        notificacion.setEstado(true);
        notificacion.setFechaCreacion(LocalDateTime.now());

        plantillaNotificationRepository.save(notificacion);

        return new MessegeGlobalDTO("Plantilla registrada correctamente");
    }

    public List<PlantillaNotificacion> leerPlantillas(String userRol) {
        ValidacionDeRoles.validarAdmin(userRol);

        List<PlantillaNotificacion> notificaciones = plantillaNotificationRepository.findAll();

        if (notificaciones.isEmpty()) {
            MessegeGlobalDTO response = new MessegeGlobalDTO("No hay plantillas registradas");
            throw new RuntimeException(response.getMessage());
        }

        return notificaciones;
    }
    
}
