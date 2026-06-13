package com.pulse_gym.ms_notifications.config;

import java.time.LocalDateTime;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.pulse_gym.lb_common.entity.notification.PlantillaDisenoEmail;
import com.pulse_gym.lb_common.entity.notification.PlantillaNotificacion;
import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;
import com.pulse_gym.ms_notifications.repository.PlantillaDisenoEmailRepository;
import com.pulse_gym.ms_notifications.repository.PlantillaNotificationRepository;

/**
 * Inicializa las plantillas y diseños de email por defecto al iniciar la aplicación.
 * Se ejecuta solo si no existen registros en la base de datos.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final PlantillaDisenoEmailRepository disenoRepository;
    private final PlantillaNotificationRepository plantillaRepository;

    public DataInitializer(PlantillaDisenoEmailRepository disenoRepository,
            PlantillaNotificationRepository plantillaRepository) {
        this.disenoRepository = disenoRepository;
        this.plantillaRepository = plantillaRepository;
    }

    @Override
    public void run(String... args) {
        // Inicializar plantillas de contenido
        if (plantillaRepository.count() == 0) {
            logger.info("Inicializando plantillas de notificacion...");
            inicializarPlantillas();
            logger.info("Plantillas de notificacion inicializadas correctamente");
        } else {
            logger.info("Las plantillas de notificacion ya existen en la base de datos");
        }

        // Inicializar diseños de email
        if (disenoRepository.count() == 0) {
            logger.info("Inicializando diseños de email por defecto...");
            inicializarDisenos();
            logger.info("Diseños de email inicializados correctamente");
        } else {
            logger.info("Los diseños de email ya existen en la base de datos");
        }
    }

    private void inicializarPlantillas() {
        logger.info("Creando plantillas de notificación...");

        PlantillaNotificacion registro = new PlantillaNotificacion();
        registro.setNombre("Registro de Usuario");
        registro.setTitulo("Bienvenido a Pulse Gym");
        registro.setDescripcion("Notificacion de bienvenida al registrar nuevo usuario");
        registro.setContenido("Hola {{username}}! Te damos la bienvenida a Pulse Gym. Tu cuenta ha sido creada exitosamente con el email {{email}}.");
        registro.setTipoPlantilla(EnumCanalNotificacion.EMAIL);
        registro.setEventoAsociado(EnumEventoAsociado.REGISTRO_USUARIO);
        registro.setEventosAsociados(Set.of(EnumEventoAsociado.REGISTRO_USUARIO));
        registro.setEstado(true);
        registro.setEliminada(false);
        registro.setFechaCreacion(LocalDateTime.now());
        PlantillaNotificacion registroGuardada = plantillaRepository.save(registro);
        logger.info("Plantilla REGISTRO_USUARIO creada con ID: {}", registroGuardada.getIdPlantilla());

        PlantillaNotificacion login = new PlantillaNotificacion();
        login.setNombre("Login de Usuario");
        login.setTitulo("Inicio de sesion detectado");
        login.setDescripcion("Notificacion de inicio de sesion");
        login.setContenido("Hola {{username}}! Se ha iniciado sesion en tu cuenta desde un nuevo dispositivo. Email: {{email}}. Si no fuiste tú, contacta a soporte.");
        login.setTipoPlantilla(EnumCanalNotificacion.EMAIL);
        login.setEventoAsociado(EnumEventoAsociado.LOGIN_USUARIO);
        login.setEventosAsociados(Set.of(EnumEventoAsociado.LOGIN_USUARIO));
        login.setEstado(true);
        login.setEliminada(false);
        login.setFechaCreacion(LocalDateTime.now());
        PlantillaNotificacion loginGuardada = plantillaRepository.save(login);
        logger.info("Plantilla LOGIN_USUARIO creada con ID: {}", loginGuardada.getIdPlantilla());

        PlantillaNotificacion welcome = new PlantillaNotificacion();
        welcome.setNombre("Bienvenida a Pulse Gym");
        welcome.setTitulo("¡Bienvenido a Pulse Gym, {{nombre}}!");
        welcome.setDescripcion("Notificación de bienvenida al completar el perfil");
        welcome.setContenido("Hola {{nombre}} {{apellido}}! Gracias por completar tu perfil. Ahora puedes acceder a todas las funcionalidades de Pulse Gym. Tu objetivo \"{{objetivo}}\" está más cerca.");
        welcome.setTipoPlantilla(EnumCanalNotificacion.EMAIL);
        welcome.setEventoAsociado(EnumEventoAsociado.WELCOME);
        welcome.setEventosAsociados(Set.of(EnumEventoAsociado.WELCOME));
        welcome.setEstado(true);
        welcome.setEliminada(false);
        welcome.setFechaCreacion(LocalDateTime.now());
        PlantillaNotificacion welcomeGuardada = plantillaRepository.save(welcome);
        logger.info("Plantilla WELCOME creada con ID: {}", welcomeGuardada.getIdPlantilla());

        logger.info("Total plantillas creadas: {}", plantillaRepository.count());
    }

    private void inicializarDisenos() {
        logger.info("Creando diseños de email...");

        PlantillaDisenoEmail defaultDiseno = new PlantillaDisenoEmail();
        defaultDiseno.setNombre("default");
        defaultDiseno.setEventoAsociado(null);
        defaultDiseno.setCanal(EnumCanalNotificacion.EMAIL);
        defaultDiseno.setColorPrincipal("#2c4b77");
        defaultDiseno.setColorSecundario("#8bb5d6");
        defaultDiseno.setColorTextoHeader("#ffffff");
        defaultDiseno.setTituloHeader("Pulse Gym");
        defaultDiseno.setSubtituloHeader("Tu bienestar, nuestra pasión");
        defaultDiseno.setColorFondoContenido("#ffffff");
        defaultDiseno.setColorTextoContenido("#5d6d7e");
        defaultDiseno.setColorFondoFooter("#f8f9fc");
        defaultDiseno.setColorTextoFooter("#9aabbb");
        defaultDiseno.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        defaultDiseno.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        defaultDiseno.setActivo(true);
        defaultDiseno.setEliminado(false);
        defaultDiseno.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(defaultDiseno);
        logger.info("Diseño DEFAULT creado");

        PlantillaDisenoEmail registroDiseno = new PlantillaDisenoEmail();
        registroDiseno.setNombre("registro");
        registroDiseno.setEventoAsociado(EnumEventoAsociado.REGISTRO_USUARIO);
        registroDiseno.setCanal(EnumCanalNotificacion.EMAIL);
        registroDiseno.setColorPrincipal("#2c4b77");
        registroDiseno.setColorSecundario("#8bb5d6");
        registroDiseno.setColorTextoHeader("#ffffff");
        registroDiseno.setTituloHeader("¡Bienvenido a Pulse Gym!");
        registroDiseno.setSubtituloHeader("Comienza tu viaje fitness hoy");
        registroDiseno.setColorFondoContenido("#ffffff");
        registroDiseno.setColorTextoContenido("#5d6d7e");
        registroDiseno.setColorFondoFooter("#f8f9fc");
        registroDiseno.setColorTextoFooter("#9aabbb");
        registroDiseno.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        registroDiseno.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        registroDiseno.setActivo(true);
        registroDiseno.setEliminado(false);
        registroDiseno.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(registroDiseno);
        logger.info("Diseño para REGISTRO_USUARIO creado");

        PlantillaDisenoEmail loginDiseno = new PlantillaDisenoEmail();
        loginDiseno.setNombre("login");
        loginDiseno.setEventoAsociado(EnumEventoAsociado.LOGIN_USUARIO);
        loginDiseno.setCanal(EnumCanalNotificacion.EMAIL);
        loginDiseno.setColorPrincipal("#f39c12");
        loginDiseno.setColorSecundario("#e67e22");
        loginDiseno.setColorTextoHeader("#ffffff");
        loginDiseno.setTituloHeader("Nuevo inicio de sesión");
        loginDiseno.setSubtituloHeader("Pulse Gym - Seguridad de tu cuenta");
        loginDiseno.setColorFondoContenido("#ffffff");
        loginDiseno.setColorTextoContenido("#5d6d7e");
        loginDiseno.setColorFondoFooter("#f8f9fc");
        loginDiseno.setColorTextoFooter("#9aabbb");
        loginDiseno.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        loginDiseno.setTextoFooterSecundario("Si no reconoces esta actividad, contacta a soporte inmediatamente");
        loginDiseno.setActivo(true);
        loginDiseno.setEliminado(false);
        loginDiseno.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(loginDiseno);
        logger.info("Diseño para LOGIN_USUARIO creado");

        PlantillaDisenoEmail welcomeDiseno = new PlantillaDisenoEmail();
        welcomeDiseno.setNombre("welcome");
        welcomeDiseno.setEventoAsociado(EnumEventoAsociado.WELCOME);
        welcomeDiseno.setCanal(EnumCanalNotificacion.EMAIL);
        welcomeDiseno.setColorPrincipal("#2c4b77");
        welcomeDiseno.setColorSecundario("#8bb5d6");
        welcomeDiseno.setColorTextoHeader("#ffffff");
        welcomeDiseno.setTituloHeader("¡Bienvenido a Pulse Gym!");
        welcomeDiseno.setSubtituloHeader("Tu viaje fitness comienza hoy");
        welcomeDiseno.setColorFondoContenido("#ffffff");
        welcomeDiseno.setColorTextoContenido("#5d6d7e");
        welcomeDiseno.setColorFondoFooter("#f8f9fc");
        welcomeDiseno.setColorTextoFooter("#9aabbb");
        welcomeDiseno.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        welcomeDiseno.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        welcomeDiseno.setActivo(true);
        welcomeDiseno.setEliminado(false);
        welcomeDiseno.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(welcomeDiseno);
        logger.info("Diseño para WELCOME creado");

        PlantillaDisenoEmail promocion = new PlantillaDisenoEmail();
        promocion.setNombre("promocion");
        promocion.setEventoAsociado(EnumEventoAsociado.PROMOTION);
        promocion.setCanal(EnumCanalNotificacion.EMAIL);
        promocion.setColorPrincipal("#ea1616");
        promocion.setColorSecundario("#c83f3f");
        promocion.setColorTextoHeader("#ffffff");
        promocion.setTituloHeader("Pulse Gym");
        promocion.setSubtituloHeader("Tu bienestar, nuestra pasión");
        promocion.setColorFondoContenido("#ffffff");
        promocion.setColorTextoContenido("#5d6d7e");
        promocion.setColorFondoFooter("#f8f9fc");
        promocion.setColorTextoFooter("#9aabbb");
        promocion.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        promocion.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        promocion.setActivo(true);
        promocion.setEliminado(false);
        promocion.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(promocion);
        logger.info("Diseño para PROMOTION creado");

        PlantillaDisenoEmail logro = new PlantillaDisenoEmail();
        logro.setNombre("logro");
        logro.setEventoAsociado(EnumEventoAsociado.ACHIEVEMENT);
        logro.setCanal(EnumCanalNotificacion.EMAIL);
        logro.setColorPrincipal("#d4af37");
        logro.setColorSecundario("#f4d03f");
        logro.setColorTextoHeader("#ffffff");
        logro.setTituloHeader("Pulse Gym");
        logro.setSubtituloHeader("¡Felicitaciones!");
        logro.setColorFondoContenido("#ffffff");
        logro.setColorTextoContenido("#5d6d7e");
        logro.setColorFondoFooter("#f8f9fc");
        logro.setColorTextoFooter("#9aabbb");
        logro.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        logro.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        logro.setActivo(true);
        logro.setEliminado(false);
        logro.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(logro);
        logger.info("Diseño para ACHIEVEMENT creado");

        PlantillaDisenoEmail pago = new PlantillaDisenoEmail();
        pago.setNombre("pago");
        pago.setEventoAsociado(EnumEventoAsociado.PAYMENT_REMINDER);
        pago.setCanal(EnumCanalNotificacion.EMAIL);
        pago.setColorPrincipal("#e67e22");
        pago.setColorSecundario("#f39c12");
        pago.setColorTextoHeader("#ffffff");
        pago.setTituloHeader("Pulse Gym");
        pago.setSubtituloHeader("Recordatorio de pago");
        pago.setColorFondoContenido("#ffffff");
        pago.setColorTextoContenido("#5d6d7e");
        pago.setColorFondoFooter("#f8f9fc");
        pago.setColorTextoFooter("#9aabbb");
        pago.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        pago.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        pago.setActivo(true);
        pago.setEliminado(false);
        pago.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(pago);
        logger.info("Diseño para PAYMENT_REMINDER creado");

        PlantillaDisenoEmail mantenimiento = new PlantillaDisenoEmail();
        mantenimiento.setNombre("mantenimiento");
        mantenimiento.setEventoAsociado(EnumEventoAsociado.MAINTENANCE_ALERT);
        mantenimiento.setCanal(EnumCanalNotificacion.EMAIL);
        mantenimiento.setColorPrincipal("#7f8c8d");
        mantenimiento.setColorSecundario("#95a5a6");
        mantenimiento.setColorTextoHeader("#ffffff");
        mantenimiento.setTituloHeader("Pulse Gym");
        mantenimiento.setSubtituloHeader("Aviso importante");
        mantenimiento.setColorFondoContenido("#ffffff");
        mantenimiento.setColorTextoContenido("#5d6d7e");
        mantenimiento.setColorFondoFooter("#f8f9fc");
        mantenimiento.setColorTextoFooter("#9aabbb");
        mantenimiento.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        mantenimiento.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        mantenimiento.setActivo(true);
        mantenimiento.setEliminado(false);
        mantenimiento.setFechaCreacion(LocalDateTime.now());
        disenoRepository.save(mantenimiento);
        logger.info("Diseño para MAINTENANCE_ALERT creado");

        logger.info("Total diseños creados: {}", disenoRepository.count());
    }
}