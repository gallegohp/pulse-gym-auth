import json
import re
from typing import Dict, Any

def parse_response(respuesta: str) -> Dict[str, Any]:
    """
    Parsea la respuesta de Gemini y la convierte a un diccionario.
    Maneja casos donde Gemini añade texto extra o markdown.
    """
    
    if not respuesta:
        return _respuesta_por_defecto("Respuesta vacía")
    
    respuesta = respuesta.strip()
    
    json_pattern = r'\{[\s\S]*\}'
    match = re.search(json_pattern, respuesta)
    
    if match:
        json_str = match.group()
        try:
            return json.loads(json_str)
        except json.JSONDecodeError as e:
            print(f"Error decodificando JSON: {e}")
    
    try:
        return json.loads(respuesta)
    except json.JSONDecodeError as e:
        print(f"Error parseando JSON: {e}")
        print(f"Respuesta recibida: {respuesta[:300]}...")
        return _respuesta_por_defecto("Error al parsear JSON")

def _respuesta_por_defecto(motivo: str) -> Dict[str, Any]:
    """Retorna una respuesta por defecto cuando el parsing falla"""
    return {
        "nombre": "Rutina Personalizada",
        "descripcion": "Rutina generada automáticamente",
        "explicacion_ia": f"No se pudo parsear la respuesta de la IA. Motivo: {motivo}",
        "dias": [
            {
                "dia": 1,
                "nombre_dia": "Lunes",
                "ejercicios": [
                    {
                        "nombre_ejercicio": "Sentadillas",
                        "series": 3,
                        "repeticiones_min": 10,
                        "repeticiones_max": 15,
                        "peso_sugerido": 0.0,
                        "descanso_segundos": 60,
                        "notas": "Ejercicio por defecto"
                    }
                ]
            }
        ]
    }