package com.pulse_gym.ms_notifications.config;

import java.time.LocalDateTime;
import java.util.List;
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
 * Inicializa los diseños de email por defecto al iniciar la aplicación.
 * Se ejecuta solo si no existen diseños en la base de datos.
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
        logger.info("Inicializando plantillas de notificación...");
        
        // Plantilla REGISTRO_USUARIO
        PlantillaNotificacion registro = new PlantillaNotificacion();
        registro.setNombre("Registro de Usuario");
        registro.setTitulo("Bienvenido a Pulse Gym");
        registro.setDescripcion("Notificacion de bienvenida al registrar nuevo usuario");
        registro.setContenido("Hola {{username}}! Te damos la bienvenida a Pulse Gym. Tu cuenta ha sido creada exitosamente.");
        registro.setTipoPlantilla(EnumCanalNotificacion.EMAIL);
        registro.setEventoAsociado(EnumEventoAsociado.REGISTRO_USUARIO);
        registro.setEventosAsociados(Set.of(EnumEventoAsociado.REGISTRO_USUARIO));
        registro.setEstado(true);
        registro.setEliminada(false);
        registro.setFechaCreacion(LocalDateTime.now());
        PlantillaNotificacion registroGuardada = plantillaRepository.save(registro);
        logger.info("Plantilla REGISTRO_USUARIO creada con ID: {}", registroGuardada.getIdPlantilla());

        // Plantilla LOGIN_USUARIO
        PlantillaNotificacion login = new PlantillaNotificacion();
        login.setNombre("Login de Usuario");
        login.setTitulo("Inicio de sesion detectado");
        login.setDescripcion("Notificacion de inicio de sesion");
        login.setContenido("Hola {{username}}! Se ha iniciado sesion en tu cuenta desde un nuevo dispositivo.");
        login.setTipoPlantilla(EnumCanalNotificacion.EMAIL);
        login.setEventoAsociado(EnumEventoAsociado.LOGIN_USUARIO);
        login.setEventosAsociados(Set.of(EnumEventoAsociado.LOGIN_USUARIO));
        login.setEstado(true);
        login.setEliminada(false);
        login.setFechaCreacion(LocalDateTime.now());
        PlantillaNotificacion loginGuardada = plantillaRepository.save(login);
        logger.info("Plantilla LOGIN_USUARIO creada con ID: {}", loginGuardada.getIdPlantilla());

        logger.info("Se crearon {} plantillas de notificacion", plantillaRepository.count());
    }

    private void inicializarDisenos() {
        // Diseño default (genérico)
        PlantillaDisenoEmail defaultDiseno = new PlantillaDisenoEmail();
        defaultDiseno.setNombre("default");
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
        disenoRepository.save(defaultDiseno);

        // Diseño para promociones
        PlantillaDisenoEmail promocion = new PlantillaDisenoEmail();
        promocion.setNombre("promocion");
        promocion.setEventoAsociado(EnumEventoAsociado.PROMOTION);
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
        disenoRepository.save(promocion);

        // Diseño para bienvenida
        PlantillaDisenoEmail bienvenida = new PlantillaDisenoEmail();
        bienvenida.setNombre("bienvenida");
        bienvenida.setEventoAsociado(EnumEventoAsociado.WELCOME);
        bienvenida.setColorPrincipal("#2c4b77");
        bienvenida.setColorSecundario("#8bb5d6");
        bienvenida.setColorTextoHeader("#ffffff");
        bienvenida.setTituloHeader("Pulse Gym");
        bienvenida.setSubtituloHeader("Tu bienestar, nuestra pasión");
        bienvenida.setColorFondoContenido("#ffffff");
        bienvenida.setColorTextoContenido("#5d6d7e");
        bienvenida.setColorFondoFooter("#f8f9fc");
        bienvenida.setColorTextoFooter("#9aabbb");
        bienvenida.setTextoFooter("© 2026 Pulse Gym - Todos los derechos reservados");
        bienvenida.setTextoFooterSecundario("Este es un mensaje automático, por favor no responder a este correo");
        bienvenida.setActivo(true);
        bienvenida.setEliminado(false);
        disenoRepository.save(bienvenida);

        // Diseño para logros
        PlantillaDisenoEmail logro = new PlantillaDisenoEmail();
        logro.setNombre("logro");
        logro.setEventoAsociado(EnumEventoAsociado.ACHIEVEMENT);
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
        disenoRepository.save(logro);

        // Diseño para recordatorios de pago
        PlantillaDisenoEmail pago = new PlantillaDisenoEmail();
        pago.setNombre("pago");
        pago.setEventoAsociado(EnumEventoAsociado.PAYMENT_REMINDER);
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
        disenoRepository.save(pago);

        // Diseño para alertas de mantenimiento
        PlantillaDisenoEmail mantenimiento = new PlantillaDisenoEmail();
        mantenimiento.setNombre("mantenimiento");
        mantenimiento.setEventoAsociado(EnumEventoAsociado.MAINTENANCE_ALERT);
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
        disenoRepository.save(mantenimiento);

        logger.info("Se crearon {} diseños de email por defecto", disenoRepository.count());
    }
}