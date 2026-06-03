package com.pulse_gym.ms_notifications.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.pulse_gym.lb_common.dto.MessegeGlobalDTO;
import com.pulse_gym.lb_common.dto.PlantillaNotificacionRequestDTO;
import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_notifications.repository.PlantillaNotificationRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlantillaService {

    /**
     * inyeccion del repositorio de plantillas de notificaciones
     */
    private final PlantillaNotificationRepository plantillaNotificationRepository;

    /**
     * Registra una nueva plantilla de notificacion en la base de datos.
     * 
     * Se valida que la peticion solo la puede hacer un Admin
     * 
     * @param request
     * @param userRol Rol del usuario que hace la peticion (desde header X-User-Rol)
     * @return MessegeGlobalDTO con un mensaje de éxito si la plantilla se registró
     *         correctamente
     */
    @Transactional
    public MessegeGlobalDTO crearPlantilla(PlantillaNotificacionRequestDTO request, String userRol) {
        ValidacionDeRoles.validarAdmin(userRol);

        PlantillaNotificacion notificacion = new PlantillaNotificacion();

        notificacion.setNombre(request.getNombre());
        notificacion.setTitulo(request.getTitulo());
        notificacion.setDescripcion(request.getDescripcion());
        notificacion.setContenido(request.getContenido());
        notificacion.setTipoPlantilla(request.getTipoPlantilla());
        notificacion.setEventoAsociado(request.getEventoAsociado());
        notificacion.setEstado(true);
        notificacion.setFechaCreacion(LocalDateTime.now());

        plantillaNotificationRepository.save(notificacion);

        return new MessegeGlobalDTO("Plantilla de notificacion registrada correctamente");

    }

    /**
     * Obtiene las plantillas de notificaciones registradas en la base de datos.
     * 
     * Se valida que la peticion solo la puede hacer un Admin
     * 
     * @param userRol Rol del usuario que hace la peticion (desde header X-User-Rol)
     * @return List<PlantillaNotificacion> con las plantillas de notificaciones
     *         registradas
     */
    public List<PlantillaNotificacion> leerPlantillas(String userRol) {
        ValidacionDeRoles.validarAdmin(userRol);

        List<PlantillaNotificacion> notificaciones = plantillaNotificationRepository.findAll();

        if (notificaciones.isEmpty()) {
            throw new RuntimeException("No hay plantillas de notificaciones registradas");
        }

        return notificaciones;
    }

    /**
     * Actualiza una plantilla de notificacion en la base de datos.
     * 
     * Se valida que la peticion solo la puede hacer un Admin
     * 
     * @param id      Identificador de la plantilla de notificacion a actualizar
     * @param request Objeto con los datos necesarios para actualizar la plantilla
     * @param userRol Rol del usuario que hace la peticion (desde header X-User-Rol)
     * @return MessegeGlobalDTO con un mensaje de éxito si la plantilla se actualizó
     *         correctamente
     */
    public MessegeGlobalDTO inactivarPlantilla(Long id, String userRol) {

        ValidacionDeRoles.validarAdmin(userRol);
        Long plantillaId = Objects.requireNonNull(id, "El id de la plantilla es obligatorio");

        PlantillaNotificacion notificacion = plantillaNotificationRepository.findById(plantillaId)
                .orElseThrow(() -> new RuntimeException("Plantilla no encontrada con id: " + plantillaId));

        notificacion.setEstado(false);

        plantillaNotificationRepository.save(notificacion);

        return new MessegeGlobalDTO("Plantilla de notificacion inactivada correctamente");
    }

    /**
     * Activa una plantilla de notificacion en la base de datos.
     * 
     * Se valida que la peticion solo la puede hacer un Admin
     *
     * @param id      Identificador de la plantilla de notificacion a activar
     * @param userRol Rol del usuario que hace la peticion (desde header X-User-Rol)
     * @return MessegeGlobalDTO con un mensaje de éxito si la plantilla se activó
     */
    public MessegeGlobalDTO activarPlantilla(Long id, String userRol) {
        ValidacionDeRoles.validarAdmin(userRol);
        Long plantillaId = Objects.requireNonNull(id, "El id de la plantilla es obligatorio");

        PlantillaNotificacion notificacion = plantillaNotificationRepository.findById(plantillaId)
                .orElseThrow(() -> new RuntimeException("Plantilla no encontrada con id: " + plantillaId));

        notificacion.setEstado(true);

        plantillaNotificationRepository.save(notificacion);

        return new MessegeGlobalDTO("Plantilla de notificacion activada correctamente");
    }

    /**
     * Método para actualizar una plantilla de notificación
     * @param id Identificador de la plantilla de notificación a actualizar
     * @param request Objeto con los datos necesarios para actualizar la plantilla
     * @param userRol Rol del usuario que hace la peticion (desde header X-User-Rol)
     * @return MessegeGlobalDTO con un mensaje de éxito si la plantilla se actualizó
     *         correctamente
     */
    public MessegeGlobalDTO actualizarPlantilla(Long id, PlantillaNotificacionRequestDTO request,
            String userRol) {
        
        ValidacionDeRoles.validarAdmin(userRol);
        Long plantillaId = Objects.requireNonNull(id, "El id de la plantilla es obligatorio");

        PlantillaNotificacion notificacion = plantillaNotificationRepository.findById(plantillaId)
                .orElseThrow(() -> new RuntimeException("Plantilla no encontrada con id: " + plantillaId));

        notificacion.setNombre(request.getNombre());
        notificacion.setTitulo(request.getTitulo());
        notificacion.setDescripcion(request.getDescripcion());
        notificacion.setContenido(request.getContenido());
        notificacion.setTipoPlantilla(request.getTipoPlantilla());
        notificacion.setEventoAsociado(request.getEventoAsociado());
        if (request.getEstado() != null) {
            notificacion.setEstado(request.getEstado());
        }

        plantillaNotificationRepository.save(notificacion);

        return new MessegeGlobalDTO("Plantilla de notificacion actualizada correctamente");

    }

}
