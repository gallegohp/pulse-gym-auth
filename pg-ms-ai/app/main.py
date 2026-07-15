from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
import logging
from typing import Dict, Any

from app.models.schemas import RutinaGeneracionRequest
from app.services.gemini_service import GeminiService

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = FastAPI(
    title="Pulse Gym - Gemini AI Service",
    description="Servicio de generación de rutinas personalizadas con Gemini AI",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

gemini_service = GeminiService()

@app.get("/")
async def root() -> Dict[str, str]:
    """Root endpoint"""
    return {"message": "Pulse Gym - Gemini AI Service", "status": "running"}

@app.get("/api/ai/health")
async def health() -> Dict[str, str]:
    """
    Verifica el estado del servicio.
    Retorna información sobre la conexión con Gemini.
    """
    status = "ok"
    gemini_status = "connected" if gemini_service.api_key else "simulation"
    return {
        "status": status,
        "service": "pg-ms-ai",
        "gemini": gemini_status,
        "model": gemini_service.model_name if gemini_service.api_key else "N/A (simulation)"
    }

@app.post("/api/ai/generar-rutina")
async def generar_rutina(request: RutinaGeneracionRequest) -> Dict[str, Any]:
    """
    Genera una rutina personalizada con Gemini.
    
    Args:
        request: Datos del socio y preferencias
        
    Returns:
        Rutina generada con ejercicios por día
        
    Raises:
        HTTPException: Si ocurre un error en la generación
    """
    try:
        contexto = request.model_dump()
        logger.info(f"Generando rutina para socio ID: {contexto.get('id_socio')}")
        
        resultado = gemini_service.generar_rutina(contexto)
        
        logger.info(f"Rutina generada exitosamente")
        return resultado
        
    except Exception as e:
        logger.error(f"Error al generar rutina: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/ai/generar-rutina-contexto")
async def generar_rutina_contexto(contexto: Dict[str, Any]) -> Dict[str, Any]:
    """
    Genera una rutina con contexto completo.
    Endpoint para pruebas avanzadas.
    
    Args:
        contexto: Diccionario completo con todos los datos
        
    Returns:
        Rutina generada
    """
    try:
        logger.info(f"Generando rutina con contexto completo")
        resultado = gemini_service.generar_rutina(contexto)
        return resultado
        
    except Exception as e:
        logger.error(f"Error al generar rutina: {str(e)}")
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(
        "app.main:app",
        host="0.0.0.0",
        port=8086,
        reload=True
    )