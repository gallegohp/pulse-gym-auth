package com.pulse_gym.lb_common.entity.notification;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumCanalNotificacion;
import com.pulse_gym.lb_common.enums.EnumEventoAsociado;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entidad para almacenar las plantillas de diseño HTML de emails.
 * Permite parametrizar colores, estilos y estructura sin modificar código Java.
 */
@Table(name = "plantilla_diseno_email")
@Entity
@Data
public class PlantillaDisenoEmail {

    /**
     * Identificador único del diseño
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diseno")
    private Long idDiseno;

    /**
     * Nombre identificador del diseño (ej: "default", "promocion", "bienvenida")
     */
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;

    /**
     * Evento asociado a este diseño
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "evento_asociado")
    private EnumEventoAsociado eventoAsociado;

    /**
     * Canal para el que aplica este diseño
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false)
    private EnumCanalNotificacion canal = EnumCanalNotificacion.EMAIL;

    /**
     * Color principal del header en formato hexadecimal
     */
    @Column(name = "color_principal")
    private String colorPrincipal = "#2c4b77";

    /**
     * Color secundario del header (para gradientes)
     */
    @Column(name = "color_secundario")
    private String colorSecundario = "#8bb5d6";

    /**
     * Color del texto del header
     */
    @Column(name = "color_texto_header")
    private String colorTextoHeader = "#ffffff";

    /**
     * Título o eslogan que aparece en el header
     */
    @Column(name = "titulo_header")
    private String tituloHeader = "Pulse Gym";

    /**
     * Subtítulo o descripción del header
     */
    @Column(name = "subtitulo_header")
    private String subtituloHeader = "Tu bienestar, nuestra pasión";

    /**
     * Color de fondo del contenido
     */
    @Column(name = "color_fondo_contenido")
    private String colorFondoContenido = "#ffffff";

    /**
     * Color del texto del contenido
     */
    @Column(name = "color_texto_contenido")
    private String colorTextoContenido = "#5d6d7e";

    /**
     * Color de fondo del footer
     */
    @Column(name = "color_fondo_footer")
    private String colorFondoFooter = "#f8f9fc";

    /**
     * Color del texto del footer
     */
    @Column(name = "color_texto_footer")
    private String colorTextoFooter = "#9aabbb";

    /**
     * Texto del footer (copyright)
     */
    @Column(name = "texto_footer")
    private String textoFooter = "© 2026 Pulse Gym - Todos los derechos reservados";

    /**
     * Texto secundario del footer
     */
    @Column(name = "texto_footer_secundario")
    private String textoFooterSecundario = "Este es un mensaje automático, por favor no responder a este correo";

    /**
     * Indica si el diseño está activo
     */
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    /**
     * Indica si el diseño fue eliminado (soft delete)
     */
    @Column(name = "eliminado", nullable = false)
    private Boolean eliminado = false;

    /**
     * Fecha de creación del diseño
     */
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    /**
     * Fecha de última modificación
     */
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
}