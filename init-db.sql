-- init-db.sql
-- Este script se ejecuta automáticamente cuando el contenedor MySQL se inicia por primera vez

-- Crear las bases de datos para cada microservicio
CREATE DATABASE IF NOT EXISTS users_db;
CREATE DATABASE IF NOT EXISTS auth_db;
CREATE DATABASE IF NOT EXISTS operations_db;
CREATE DATABASE IF NOT EXISTS notifications_db;
CREATE DATABASE IF NOT EXISTS reports_db;

-- Otorgar permisos al usuario pulsegym sobre todas las bases de datos
GRANT ALL PRIVILEGES ON users_db.* TO 'pulsegym'@'%';
GRANT ALL PRIVILEGES ON auth_db.* TO 'pulsegym'@'%';
GRANT ALL PRIVILEGES ON operations_db.* TO 'pulsegym'@'%';
GRANT ALL PRIVILEGES ON notifications_db.* TO 'pulsegym'@'%';
GRANT ALL PRIVILEGES ON reports_db.* TO 'pulsegym'@'%';

-- Aplicar los cambios
FLUSH PRIVILEGES;