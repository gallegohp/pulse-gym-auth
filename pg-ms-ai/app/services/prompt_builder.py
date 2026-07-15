from typing import Dict, Any, List

def build_prompt(contexto: Dict[str, Any]) -> str:
    """
    Construye el prompt para Gemini basado en el contexto del socio.
    Este prompt está optimizado para obtener rutinas personalizadas.
    """
    
    ejercicios_disponibles = contexto.get("ejerciciosDisponibles", [])
    
    prompt = f"""
Eres un entrenador personal experto en fitness y nutrición con amplia experiencia en la creación de rutinas personalizadas.

DATOS DEL SOCIO:
- Nombre: {contexto.get('nombre', 'No especificado')}
- Edad: {contexto.get('edad', 0)} años
- Peso: {contexto.get('peso', 'No especificado')} kg
- Estatura: {contexto.get('estatura', 'No especificado')} cm
- Nivel de experiencia: {contexto.get('nivelExperiencia', 'No especificado')}
- Objetivo principal: {contexto.get('objetivoPrincipal', 'No especificado')}
- Porcentaje de grasa: {contexto.get('porcentajeGrasa', 'No especificado')}%
- Porcentaje de músculo: {contexto.get('porcentajeMusculo', 'No especificado')}%
- Lesiones previas: {contexto.get('lesionesPrevias', 'Ninguna')}
- Condiciones médicas: {contexto.get('condicionesCronicas', 'Ninguna')}
- Alergias: {contexto.get('alergias', 'Ninguna')}
- Días disponibles por semana: {contexto.get('diasPorSemana', 3)}
- Duración en semanas: {contexto.get('duracionSemanas', 4)}
- Equipamiento preferido: {contexto.get('preferenciasEquipamiento', 'Cualquiera')}
- Ejercicios a evitar: {contexto.get('evitarEjercicios', 'Ninguno')}
- Grupos musculares a priorizar: {contexto.get('preferenciasGruposMusculares', 'Todos')}
- Objetivo específico: {contexto.get('objetivoEspecifico', 'No especificado')}
- Incluir cardio: {contexto.get('incluirCardio', True)}

EJERCICIOS DISPONIBLES ({len(ejercicios_disponibles)} ejercicios):
"""
    
    # Agregar hasta 50 ejercicios para no sobrecargar el prompt
    for ej in ejercicios_disponibles[:50]:
        prompt += f"- {ej.get('nombre')} | Grupo: {ej.get('grupoMuscular')} | Equipo: {ej.get('equipoNecesario', 'Ninguno')} | Dificultad: {ej.get('dificultad', 3)}/5\n"
    
    prompt += """

REGLAS IMPORTANTES:
1. La rutina debe ser SEGURA considerando lesiones y condiciones médicas
2. Ajustada al NIVEL DE EXPERIENCIA del socio
3. Debe ayudar a alcanzar el OBJETIVO PRINCIPAL
4. Distribuir los ejercicios equitativamente entre los días
5. Incluir calentamiento y enfriamiento
6. NO incluir ejercicios que el socio deba evitar
7. Utilizar el equipamiento disponible
8. Progresión adecuada en dificultad

RESPONDE EXCLUSIVAMENTE CON UN JSON VÁLIDO en el siguiente formato:
{
    "nombre": "Nombre atractivo de la rutina",
    "descripcion": "Descripción detallada de la rutina",
    "explicacion_ia": "Explicación de por qué esta rutina es adecuada para el socio",
    "dias": [
        {
            "dia": 1,
            "nombre_dia": "Lunes",
            "ejercicios": [
                {
                    "nombre_ejercicio": "Press de Banca",
                    "series": 4,
                    "repeticiones_min": 8,
                    "repeticiones_max": 12,
                    "peso_sugerido": 20.0,
                    "descanso_segundos": 60,
                    "notas": "Mantener la espalda plana contra el banco"
                }
            ]
        }
    ]
}

SOLO RESPONDE CON EL JSON. Sin texto adicional, sin markdown, sin explicaciones fuera del JSON.
"""
    
    return prompt