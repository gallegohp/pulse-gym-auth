package com.pulse_gym.ms_users.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pulse_gym.lb_common.dto.DetalleRutinaResponseDTO;
import com.pulse_gym.lb_common.dto.RutinaAjusteRequestDTO;
import com.pulse_gym.lb_common.dto.RutinaGeneracionRequestDTO;
import com.pulse_gym.lb_common.dto.RutinaGeneracionResponseDTO;
import com.pulse_gym.lb_common.dto.RutinaHistorialResponseDTO;
import com.pulse_gym.lb_common.exception.SecurityAuthorizationException;
import com.pulse_gym.lb_common.services.ValidacionDeRoles;
import com.pulse_gym.ms_users.service.RutinaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rutinas")
@RequiredArgsConstructor
public class RutinaController {

    /** Servicio de rutinas para operaciones de negocio */
    private final RutinaService rutinaService;

    /**
     * Genera una rutina de ejemplo (mock) para propósitos de desarrollo
     * 
     * @param idSocio ID del socio
     * @param nombre  Nombre del socio
     * @return DTO con la rutina de ejemplo
     */
    private RutinaGeneracionResponseDTO generarRutinaMock(Long idSocio, String nombre) {
        RutinaGeneracionResponseDTO rutina = new RutinaGeneracionResponseDTO();
        rutina.setIdRutina(new Random().nextLong(1000));
        rutina.setNombre("Rutina Personalizada - " + nombre);
        rutina.setDescripcion("Rutina generada por IA basada en tus objetivos y nivel de experiencia");
        rutina.setExplicacionIA(
                "Esta rutina ha sido diseñada específicamente para ti, considerando tu nivel actual y tus objetivos a corto plazo.");
        rutina.setVersion(1);
        rutina.setGeneradaPorIA(true);
        rutina.setFechaGeneracion(LocalDateTime.now());

        List<DetalleRutinaResponseDTO> detalles = new ArrayList<>();

        detalles.add(crearDetalleMock("Press de Banca", "PECHO", 1, 1, 4, 8, 12, new BigDecimal("20.00"), 60,
                "Mantén la espalda plana"));
        detalles.add(crearDetalleMock("Press Inclinado con Mancuernas", "PECHO", 1, 2, 3, 10, 14,
                new BigDecimal("15.00"), 60, "Controla el movimiento"));
        detalles.add(crearDetalleMock("Aperturas con Mancuernas", "PECHO", 1, 3, 3, 12, 15, new BigDecimal("10.00"), 45,
                "No bajes demasiado"));
        detalles.add(crearDetalleMock("Fondos en Paralelas", "BRAZOS", 1, 4, 3, 8, 12, new BigDecimal("0.00"), 60,
                "Usa ayuda si es necesario"));

        detalles.add(crearDetalleMock("Dominadas", "ESPALDA", 2, 1, 3, 6, 10, new BigDecimal("0.00"), 60,
                "Usa banda elástica si es necesario"));
        detalles.add(crearDetalleMock("Remo con Barra", "ESPALDA", 2, 2, 4, 8, 12, new BigDecimal("25.00"), 60,
                "Mantén la espalda recta"));
        detalles.add(crearDetalleMock("Jalón al Pecho", "ESPALDA", 2, 3, 3, 10, 14, new BigDecimal("30.00"), 60,
                "Controla la bajada"));
        detalles.add(crearDetalleMock("Curl de Bíceps con Barra", "BRAZOS", 2, 4, 3, 10, 14, new BigDecimal("15.00"),
                45, "Sin balanceo"));

        detalles.add(crearDetalleMock("Sentadillas", "PIERNAS", 3, 1, 4, 8, 12, new BigDecimal("30.00"), 60,
                "Baja hasta 90 grados"));
        detalles.add(crearDetalleMock("Prensa de Piernas", "PIERNAS", 3, 2, 3, 10, 14, new BigDecimal("40.00"), 60,
                "Controla el peso"));
        detalles.add(crearDetalleMock("Extensiones de Cuádriceps", "PIERNAS", 3, 3, 3, 12, 16, new BigDecimal("20.00"),
                45, "Movimiento controlado"));
        detalles.add(crearDetalleMock("Curl de Isquiotibiales", "PIERNAS", 3, 4, 3, 12, 16, new BigDecimal("15.00"), 45,
                "Sin rebotes"));

        detalles.add(crearDetalleMock("Press de Hombros con Barra", "HOMBROS", 4, 1, 3, 8, 12, new BigDecimal("15.00"),
                60, "No bajes demasiado"));
        detalles.add(crearDetalleMock("Elevaciones Laterales", "HOMBROS", 4, 2, 3, 12, 16, new BigDecimal("8.00"), 45,
                "Peso ligero"));
        detalles.add(crearDetalleMock("Plancha", "CORE", 4, 3, 3, 30, 45, new BigDecimal("0.00"), 30,
                "Mantén el cuerpo recto"));
        detalles.add(crearDetalleMock("Elevación de Piernas", "CORE", 4, 4, 3, 12, 16, new BigDecimal("0.00"), 30,
                "Controla el movimiento"));

        detalles.add(crearDetalleMock("Caminata en Cinta", "CARDIO", 5, 1, 1, 20, 30, new BigDecimal("0.00"), 0,
                "Ritmo moderado"));
        detalles.add(crearDetalleMock("Burpees", "FULL_BODY", 5, 2, 3, 10, 15, new BigDecimal("0.00"), 45,
                "Mantén el ritmo"));
        detalles.add(crearDetalleMock("Kettlebell Swings", "FULL_BODY", 5, 3, 3, 12, 16, new BigDecimal("12.00"), 45,
                "Usa la cadera"));

        rutina.setDetalles(detalles);
        return rutina;
    }

    /**
     * Crea un detalle de rutina de ejemplo (mock) para propósitos de desarrollo
     * 
     * @param nombreEjercicio  Nombre del ejercicio
     * @param grupoMuscular    Grupo muscular del ejercicio
     * @param diaSemana        Día de la semana (1-7)
     * @param orden            Orden de ejecución
     * @param series           Número de series
     * @param repeticionesMin  Repeticiones mínimas
     * @param repeticionesMax  Repeticiones máximas
     * @param pesoSugerido     Peso sugerido
     * @param descansoSegundos Tiempo de descanso en segundos
     * @param notas            Notas adicionales
     * @return DTO del detalle de rutina
     */
    private DetalleRutinaResponseDTO crearDetalleMock(
            String nombreEjercicio,
            String grupoMuscular,
            Integer diaSemana,
            Integer orden,
            Integer series,
            Integer repeticionesMin,
            Integer repeticionesMax,
            BigDecimal pesoSugerido,
            Integer descansoSegundos,
            String notas) {

        DetalleRutinaResponseDTO detalle = new DetalleRutinaResponseDTO();
        detalle.setIdDetalle(new Random().nextLong(10000));
        detalle.setIdEjercicio(new Random().nextLong(100));
        detalle.setNombreEjercicio(nombreEjercicio);
        detalle.setGrupoMuscular(grupoMuscular);
        detalle.setUrlImagen(
                "https://images.pulsegym.com/ejercicios/" + nombreEjercicio.toLowerCase().replace(" ", "-") + ".jpg");
        detalle.setUrlVideo("https://youtube.com/watch?v=" + nombreEjercicio.toLowerCase().replace(" ", "-"));
        detalle.setDiaSemana(diaSemana);
        detalle.setOrden(orden);
        detalle.setSeries(series);
        detalle.setRepeticionesMin(repeticionesMin);
        detalle.setRepeticionesMax(repeticionesMax);
        detalle.setPesoSugerido(pesoSugerido);
        detalle.setDescansoSegundos(descansoSegundos);
        detalle.setNotas(notas);
        return detalle;
    }

    /**
     * Genera una rutina de entrenamiento personalizada (MODO MOCK)
     * 
     * @param request           Datos del socio y preferencias
     * @param userRol           Rol del usuario autenticado (header)
     * @param userIdAutenticado ID del usuario autenticado (header)
     * @return DTO con la rutina generada
     */
    @PostMapping("/generar")
    public ResponseEntity<RutinaGeneracionResponseDTO> generarRutina(
            @Valid @RequestBody RutinaGeneracionRequestDTO request,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {

        try {
            RutinaGeneracionResponseDTO mock = generarRutinaMock(
                    request.getIdSocio(),
                    "Socio ID: " + request.getIdSocio());
            mock.setGeneradaPorIA(true);
            mock.setDescripcion(
                    "🔵 [MODO MOCK] Rutina generada con datos de prueba. Conecta el servicio Python para rutinas reales.");
            return ResponseEntity.status(HttpStatus.CREATED).body(mock);

        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al generar la rutina: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todas las rutinas del usuario autenticado (MODO MOCK)
     * 
     * @param userIdAutenticado ID del usuario autenticado (header)
     * @param userRol           Rol del usuario autenticado (header)
     * @return Mapa con la lista de rutinas
     */
    @GetMapping("/mis-rutinas")
    public ResponseEntity<Map<String, Object>> obtenerMisRutinas(
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        try {
            List<RutinaGeneracionResponseDTO> rutinas = new ArrayList<>();
            rutinas.add(generarRutinaMock(userIdAutenticado, "Rutina Activa"));
            rutinas.add(generarRutinaMock(userIdAutenticado, "Rutina Anterior"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rutinas encontradas (MODO MOCK)");
            response.put("count", rutinas.size());
            response.put("data", rutinas);
            return ResponseEntity.ok(response);

        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener rutinas", e);
        }
    }

    /**
     * Obtiene las rutinas de un socio específico (MODO MOCK)
     * 
     * @param idSocio           ID del socio a consultar
     * @param userRol           Rol del usuario autenticado (header)
     * @param userIdAutenticado ID del usuario autenticado (header)
     * @return Mapa con la lista de rutinas del socio
     */
    @GetMapping("/socio/{idSocio}")
    public ResponseEntity<Map<String, Object>> obtenerRutinasSocio(
            @PathVariable Long idSocio,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {

        try {
            List<RutinaGeneracionResponseDTO> rutinas = new ArrayList<>();
            rutinas.add(generarRutinaMock(idSocio, "Rutina Activa"));
            rutinas.add(generarRutinaMock(idSocio, "Rutina Anterior"));

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rutinas del socio encontradas (MODO MOCK)");
            response.put("count", rutinas.size());
            response.put("data", rutinas);
            return ResponseEntity.ok(response);

        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener rutinas del socio", e);
        }
    }

    /**
     * Obtiene una rutina específica por su ID (MODO MOCK)
     * 
     * @param idRutina          ID de la rutina a consultar
     * @param userRol           Rol del usuario autenticado (header)
     * @param userIdAutenticado ID del usuario autenticado (header)
     * @return DTO de la rutina
     */
    @GetMapping("/{idRutina}")
    public ResponseEntity<RutinaGeneracionResponseDTO> obtenerRutina(
            @PathVariable Long idRutina,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {

        try {
            RutinaGeneracionResponseDTO mock = generarRutinaMock(userIdAutenticado, "Detalle Rutina");
            mock.setIdRutina(idRutina);
            return ResponseEntity.ok(mock);

        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener la rutina", e);
        }
    }

    /**
     * Ajusta un detalle de una rutina (MODO MOCK)
     * 
     * @param idRutina ID de la rutina a ajustar
     * @param request  DTO con los datos a ajustar
     * @param userRol  Rol del usuario autenticado (header)
     * @return Mapa con el resultado del ajuste
     */
    @PutMapping("/{idRutina}/ajustar")
    public ResponseEntity<Map<String, Object>> ajustarRutina(
            @PathVariable Long idRutina,
            @Valid @RequestBody RutinaAjusteRequestDTO request,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol) {

        try {
            ValidacionDeRoles.validarEntrenador(userRol);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Rutina ajustada correctamente (MODO MOCK)");
            response.put("idRutina", idRutina);
            response.put("idDetalle", request.getIdDetalle());
            response.put("motivo", request.getMotivo());
            return ResponseEntity.ok(response);

        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al ajustar la rutina", e);
        }
    }

    /**
     * Obtiene el historial de versiones de una rutina (MODO MOCK)
     * 
     * @param idRutina          ID de la rutina a consultar
     * @param userRol           Rol del usuario autenticado (header)
     * @param userIdAutenticado ID del usuario autenticado (header)
     * @return Mapa con el historial de versiones
     */
    @GetMapping("/{idRutina}/historial")
    public ResponseEntity<Map<String, Object>> obtenerHistorialRutina(
            @PathVariable Long idRutina,
            @RequestHeader(value = "X-User-Rol", required = false) String userRol,
            @RequestHeader(value = "X-User-Id", required = false) Long userIdAutenticado) {

        try {
            List<RutinaHistorialResponseDTO> historial = new ArrayList<>();

            RutinaHistorialResponseDTO version1 = new RutinaHistorialResponseDTO();
            version1.setIdHistorial(1L);
            version1.setVersion(1);
            version1.setDatosJson("{\"nombre\":\"Rutina inicial\", \"dias\":[...]}");
            version1.setModificadoPor("IA");
            version1.setMotivo("Generación inicial");
            version1.setFechaModificacion(LocalDateTime.now().minusDays(5));
            historial.add(version1);

            RutinaHistorialResponseDTO version2 = new RutinaHistorialResponseDTO();
            version2.setIdHistorial(2L);
            version2.setVersion(2);
            version2.setDatosJson("{\"nombre\":\"Rutina ajustada\", \"dias\":[...]}");
            version2.setModificadoPor("Entrenador Juan");
            version2.setMotivo("Ajuste de series y repeticiones");
            version2.setFechaModificacion(LocalDateTime.now().minusDays(2));
            historial.add(version2);

            RutinaHistorialResponseDTO version3 = new RutinaHistorialResponseDTO();
            version3.setIdHistorial(3L);
            version3.setVersion(3);
            version3.setDatosJson("{\"nombre\":\"Rutina final\", \"dias\":[...]}");
            version3.setModificadoPor("Entrenador Juan");
            version3.setMotivo("Optimización final");
            version3.setFechaModificacion(LocalDateTime.now().minusDays(1));
            historial.add(version3);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Historial de versiones (MODO MOCK)");
            response.put("count", historial.size());
            response.put("data", historial);
            return ResponseEntity.ok(response);

        } catch (SecurityAuthorizationException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al obtener historial", e);
        }
    }
}