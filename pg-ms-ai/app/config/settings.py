import os
from dotenv import load_dotenv

load_dotenv()

class Settings:
    """Configuración del servicio de IA"""
    
    GEMINI_API_KEY: str = os.getenv("GEMINI_API_KEY", "")
    MODEL_NAME: str = os.getenv("MODEL_NAME", "gemini-2.0-flash")
    
    TEMPERATURE: float = float(os.getenv("TEMPERATURE", "0.7"))
    MAX_OUTPUT_TOKENS: int = int(os.getenv("MAX_OUTPUT_TOKENS", "8192"))
    TOP_P: float = float(os.getenv("TOP_P", "0.95"))
    TOP_K: int = int(os.getenv("TOP_K", "40"))

settings = Settings()

if not settings.GEMINI_API_KEY:
    print("⚠️  ADVERTENCIA: GEMINI_API_KEY no configurada en el archivo .env")
    print("⚠️  El servicio funcionará en MODO SIMULACIÓN")
else:
    print(f"✅ Gemini configurado con modelo: {settings.MODEL_NAME}")