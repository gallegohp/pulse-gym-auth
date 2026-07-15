import google.generativeai as genai
import logging
from typing import Dict, Any

from app.config.settings import settings
from app.services.prompt_builder import build_prompt
from app.services.parser import parse_response

logger = logging.getLogger(__name__)

class GeminiService:
    def __init__(self):
        self.api_key = settings.GEMINI_API_KEY
        self.model_name = settings.MODEL_NAME
        
        if self.api_key:
            genai.configure(api_key=self.api_key)
            self.model = genai.GenerativeModel(self.model_name)
            logger.info(f"Gemini configurado con modelo: {self.model_name}")
        else:
            logger.warning("GEMINI_API_KEY no configurada. Usando MODO SIMULACIÓN.")
            self.model = None
    
    def generar_rutina(self, contexto: Dict[str, Any]) -> Dict[str, Any]:
        if not self.api_key or not self.model:
            logger.info("🔄 Usando MODO SIMULACIÓN para generar rutina")
            return self._generar_rutina_simulada(contexto)
        
        try:
            prompt = build_prompt(contexto)
            logger.info(f"Enviando prompt a Gemini (tamaño: {len(prompt)} caracteres)")
            
            generation_config = {
                "temperature": settings.TEMPERATURE,
                "max_output_tokens": settings.MAX_OUTPUT_TOKENS,
                "top_p": settings.TOP_P,
                "top_k": settings.TOP_K,
            }
            
            response = self.model.generate_content(prompt, generation_config=generation_config)
            respuesta_texto = response.text
            logger.info(f"Respuesta recibida de Gemini (tamaño: {len(respuesta_texto)} caracteres)")
            
            resultado = parse_response(respuesta_texto)
            
            if not resultado.get("dias") or len(resultado.get("dias", [])) == 0:
                logger.warning("La respuesta no tiene días de entrenamiento. Usando datos por defecto.")
                return self._generar_rutina_simulada(contexto)
            
            return resultado
            
        except Exception as e:
            logger.error(f"❌ Error al llamar a Gemini: {str(e)}")
            return self._generar_rutina_simulada(contexto)
    
    def _generar_rutina_simulada(self, contexto: Dict[str, Any]) -> Dict[str, Any]:
        dias = contexto.get('diasPorSemana', 3)
        nombre = contexto.get('nombre', 'Socio')
        objetivo = contexto.get('objetivoPrincipal', 'mejorar condición física')
        
        ejercicios = [
            {"nombre": "Sentadillas con peso corporal", "grupo": "PIERNAS"},
            {"nombre": "Flexiones de brazos", "grupo": "PECHO"},
            {"nombre": "Dominadas asistidas", "grupo": "ESPALDA"},
            {"nombre": "Plancha", "grupo": "CORE"},
            {"nombre": "Prensa de hombros con mancuernas", "grupo": "HOMBROS"},
            {"nombre": "Curl de bíceps con mancuernas", "grupo": "BRAZOS"},
            {"nombre": "Caminata en cinta", "grupo": "CARDIO"},
            {"nombre": "Peso muerto", "grupo": "ESPALDA"},
            {"nombre": "Zancadas", "grupo": "PIERNAS"},
            {"nombre": "Remo con barra", "grupo": "ESPALDA"},
        ]
        
        dias_rutina = []
        nombres_dias = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"]
        
        for i in range(min(dias, len(nombres_dias))):
            ejercicios_dia = []
            start_idx = (i * 3) % len(ejercicios)
            
            for j in range(3):
                idx = (start_idx + j) % len(ejercicios)
                ej = ejercicios[idx]
                ejercicios_dia.append({
                    "nombre_ejercicio": ej["nombre"],
                    "series": 3 + (i % 2),
                    "repeticiones_min": 8 if i < 2 else 10,
                    "repeticiones_max": 12 if i < 2 else 15,
                    "peso_sugerido": 0.0,
                    "descanso_segundos": 60,
                    "notas": f"Ejercicio para {ej['grupo']}. [MODO SIMULACIÓN]"
                })
            
            dias_rutina.append({
                "dia": i + 1,
                "nombre_dia": nombres_dias[i],
                "ejercicios": ejercicios_dia
            })
        
        if contexto.get('incluirCardio', True) and dias_rutina:
            cardio_ejercicio = {
                "nombre_ejercicio": "Cardio - Caminata/Trote",
                "series": 1,
                "repeticiones_min": 20,
                "repeticiones_max": 30,
                "peso_sugerido": 0.0,
                "descanso_segundos": 0,
                "notas": "Cardio al final del entrenamiento [MODO SIMULACIÓN]"
            }
            dias_rutina[-1]["ejercicios"].append(cardio_ejercicio)
        
        return {
            "nombre": f"Rutina de {objetivo} para {nombre}",
            "descripcion": f"Rutina personalizada de {dias} días para {objetivo}",
            "explicacion_ia": "[MODO SIMULACIÓN] Configura GEMINI_API_KEY en el .env para obtener rutinas reales.",
            "dias": dias_rutina
        }