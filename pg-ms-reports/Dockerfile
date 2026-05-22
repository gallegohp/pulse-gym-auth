# Paso 1: Usar una imagen de Java (el entorno)
FROM eclipse-temurin:21-jdk-alpine

# Paso 2: Crear un directorio para la app
WORKDIR /app

# Paso 3: Copiar el archivo .jar de tu carpeta target al contenedor
COPY target/*.jar app.jar

# Paso 4: Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]