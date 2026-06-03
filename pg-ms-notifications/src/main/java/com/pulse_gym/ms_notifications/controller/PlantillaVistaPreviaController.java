package com.pulse_gym.ms_notifications.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pulse_gym.ms_notifications.services.PlantillaRenderService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plantilla")
public class PlantillaVistaPreviaController {
    
    private final PlantillaRenderService renderService;
    
    @PostMapping("/vista-previa")
    public ResponseEntity<Map<String, Object>> vistaPrevia(@RequestBody VistaPreviaRequest request) {
        
        // Extraer variables
        Set<String> variables = renderService.extraerVariables(request.getContenido());
        
        // Generar valores de ejemplo
        Map<String, Object> valoresEjemplo = renderService.generarValoresEjemplo(variables);
        
        // Si el usuario envió valores personalizados, usarlos
        if (request.getValoresPrueba() != null) {
            valoresEjemplo.putAll(request.getValoresPrueba());
        }
        
        // Renderizar
        String contenidoVista = renderService.renderizar(request.getContenido(), valoresEjemplo);
        
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("contenido", contenidoVista);
        respuesta.put("variables_encontradas", variables);
        respuesta.put("valores_usados", valoresEjemplo);
        
        return ResponseEntity.ok(respuesta);
    }
}

@Data
class VistaPreviaRequest {
    private String contenido;
    private Map<String, Object> valoresPrueba;
}