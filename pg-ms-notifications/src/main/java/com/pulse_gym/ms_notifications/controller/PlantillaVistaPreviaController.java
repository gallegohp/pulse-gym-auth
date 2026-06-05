package com.pulse_gym.ms_notifications.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_notifications.services.PlantillaRenderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plantilla")
public class PlantillaVistaPreviaController {

    /*
     * Inyeccion de servicio de renderizado de plantillas
     */
    private final PlantillaRenderService renderService;

    /**
     * Genera una vista previa de plantilla con valores de ejemplo
     *
     * @param request Datos de la plantilla
     * @param userRol Rol del usuario autenticado
     * @return Contenido renderizado y variables detectadas
     */
    @PostMapping("/vista-previa")
    public ResponseEntity<Map<String, Object>> vistaPrevia(
            @RequestBody VistaPreviaRequest request,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        ValidacionDeRoles.validarAdmin(userRol);

        Set<String> variables = renderService.extraerVariables(request.getContenido());
        Map<String, Object> valoresEjemplo = renderService.generarValoresEjemplo(variables);

        if (request.getValoresPrueba() != null) {
            valoresEjemplo.putAll(request.getValoresPrueba());
        }

        String contenidoVista = renderService.renderizar(request.getContenido(), valoresEjemplo);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("contenido", contenidoVista);
        respuesta.put("variables_encontradas", variables);
        respuesta.put("valores_usados", valoresEjemplo);

        return ResponseEntity.ok(respuesta);
    }
}
