package com.pulse_gym.lb_common.entity.user;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pulse_gym.lb_common.enums.EnumEstadoUsuario;
import com.pulse_gym.lb_common.enums.NivelExperiencia;
import com.pulse_gym.lb_common.enums.Turno;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario_perfil")
public class UsuarioPerfil {

    /**
     * Identificador único del usuario, generado automáticamente por la base de
     * datos
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    /**
     * Nombres del usuario
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Correo de usuario
     */
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    /**
     * Apellidos del usuario
     */
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    /**
     * Estado del usuario (ACTIVO/INACTIVO)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EnumEstadoUsuario estado = EnumEstadoUsuario.ACTIVO;

    /**
     * Número de teléfono de contacto del usuario
     */
    @Column(name = "telefono", length = 20)
    private String telefono;

    /**
     * Número de documento de identidad, único para cada usuario registrado
     */
    @Column(name = "documento_identidad", nullable = false, unique = true, length = 20)
    private String documentoIdentidad;

    /**
     * URL o ruta de almacenamiento de la foto de perfil del usuario
     */
    @Column(name = "foto_url", nullable = false, length = 255)
    private String fotoUrl;

    /**
     * Fecha en la que el usuario fue contratado (aplica para personal
     * administrativo y entrenadores)
     */
    @Column(name = "fecha_contratacion", nullable = false)
    private LocalDate fechaContratacion;

    /**
     * Especialidad o enfoque profesional del entrenador
     */
    @Column(name = "especialidad", nullable = false, length = 100)
    private String especialidad;

    /**
     * Cantidad de años de experiencia laboral del usuario en su campo
     */
    @Column(name = "años_experiencia", nullable = false)
    private Short anosExperiencia;

    /**
     * Horarios y días en los que el usuario está disponible para laborar o asistir
     */
    @Column(name = "horario_disponibilidad", nullable = false, length = 255)
    private String horarioDisponibilidad;

    /**
     * Costo o cobro por hora asignado (relevante para el cálculo de nómina de
     * entrenadores)
     */
    @Column(name = "tarifa_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaHora;

    /**
     * Turno laboral o de asistencia asignado en el sistema
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "turno", nullable = false, columnDefinition = "ENUM('mañana', 'tarde', 'noche')")
    private Turno turno;

    /**
     * Fecha de nacimiento del usuario
     */
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    /**
     * Nombre completo de la persona designada para casos de emergencia
     */
    @Column(name = "contacto_emergencia_nombre", nullable = false, length = 100)
    private String contactoEmergenciaNombre;

    /**
     * Teléfono de la persona designada para el contacto de emergencia
     */
    @Column(name = "contacto_emergencia_telefono", nullable = false, length = 20)
    private String contactoEmergenciaTelefono;

    /**
     * Meta física o deportiva que el socio busca alcanzar en el gimnasio
     */
    @Column(name = "objetivo_principal", nullable = false, length = 255)
    private String objetivoPrincipal;

    /**
     * Nivel de conocimiento o condición física actual que posee el usuario
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_experiencia", nullable = false, columnDefinition = "ENUM('novato', 'intermedio', 'avanzado')")
    private NivelExperiencia nivelExperiencia;

    /**
     * Fecha y hora exacta en la que se registró el perfil. No se puede modificar
     * tras su creación
     */
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Identificador de la sede física a la cual pertenece o asiste el usuario
     */
    @Column(name = "id_sede")
    private Integer idSede;

    /**
     * Método callback de JPA que se ejecuta automáticamente antes de persistir el
     * registro,
     * asignando la fecha y hora actual del sistema a la propiedad fechaRegistro.
     */
    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }

    /**
     * Lista de documentos legales asociados al usuario
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentoLegal> documentosLegales = new ArrayList<>();

    /**
     * Agrega un documento legal a la lista de documentos asociados al usuario y
     * establece la relación bidireccional entre el usuario y el documento.
     * 
     * @param documento El documento legal a agregar al usuario
     */
    public void addDocumentoLegal(DocumentoLegal documento) {
        documentosLegales.add(documento);
        documento.setUsuario(this);
    }

    /**
     * Elimina un documento legal de la lista de documentos asociados al usuario y
     * rompe la relación bidireccional entre el usuario y el documento.
     * 
     * @param documento El documento legal a eliminar del usuario
     */
    public void removeDocumentoLegal(DocumentoLegal documento) {
        documentosLegales.remove(documento);
        documento.setUsuario(null);
    }

    /**
     * Lista de certificaciones asociadas al usuario
     */
    @OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Certificacion> certificaciones = new ArrayList<>();

    /**
     * Agrega una certificación a la lista de certificaciones asociadas al usuario y
     * establece la relación bidireccional entre el usuario y la certificación.
     * 
     * @param certificacion La certificación a agregar al usuario
     */
    public void addCertificacion(Certificacion certificacion) {
        certificaciones.add(certificacion);
        certificacion.setEntrenador(this);
    }

    /**
     * Elimina una certificación de la lista de certificaciones asociadas al usuario
     * y
     * rompe la relación bidireccional entre el usuario y la certificación.
     * 
     * @param certificacion La certificación a eliminar del usuario
     */
    public void removeCertificacion(Certificacion certificacion) {
        certificaciones.remove(certificacion);
        certificacion.setEntrenador(null);
    }

    @OneToOne(mappedBy = "socio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PerfilMedico perfilMedico;
}