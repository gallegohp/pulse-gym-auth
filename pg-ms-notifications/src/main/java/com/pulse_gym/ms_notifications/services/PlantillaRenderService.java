package com.pulse_gym.ms_notifications.services;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PlantillaRenderService {
    
    private static final Pattern PATRON_VARIABLE = Pattern.compile("\\{\\{(\\w+)\\}\\}");
    
    public Set<String> extraerVariables(String contenido) {
        Set<String> variables = new HashSet<>();
        if (contenido == null || contenido.isEmpty()) {
            return variables;
        }
        Matcher matcher = PATRON_VARIABLE.matcher(contenido);
        while (matcher.find()) {
            variables.add(matcher.group(1));
        }
        return variables;
    }
    
    public String renderizar(String contenido, Map<String, Object> contexto) {
        if (contenido == null || contenido.isEmpty()) {
            return contenido;
        }
        
        String resultado = contenido;
        
        for (Map.Entry<String, Object> entry : contexto.entrySet()) {
            String variable = "{{" + entry.getKey() + "}}";
            String valor = entry.getValue() != null ? entry.getValue().toString() : "";
            resultado = resultado.replace(variable, valor);
        }
        
        return resultado;
    }
    
    public Map<String, Object> generarValoresEjemplo(Set<String> variables) {
        Map<String, Object> ejemplos = getValoresEjemploPorDefecto();
        
        // Filtrar solo las variables que necesita la plantilla
        Map<String, Object> resultado = new HashMap<>();
        for (String variable : variables) {
            resultado.put(variable, ejemplos.getOrDefault(variable, "[EJEMPLO_" + variable.toUpperCase() + "]"));
        }
        
        return resultado;
    }
    
    private Map<String, Object> getValoresEjemploPorDefecto() {
        Map<String, Object> ejemplos = new HashMap<>();
        ejemplos.put("nombre", "María González");
        ejemplos.put("apellido", "González");
        ejemplos.put("email", "maria@ejemplo.com");
        ejemplos.put("telefono", "+57 300 123 4567");
        ejemplos.put("fecha_registro", "15/01/2024");
        ejemplos.put("fecha_vencimiento", "15/04/2025");
        ejemplos.put("monto", "$150.00");
        ejemplos.put("plan", "Premium");
        ejemplos.put("dias_restantes", "5");
        ejemplos.put("rol", "SOCIO");
        ejemplos.put("objetivo", "Aumentar masa muscular");
        ejemplos.put("nivel_experiencia", "INTERMEDIO");
        return ejemplos;
    }
}