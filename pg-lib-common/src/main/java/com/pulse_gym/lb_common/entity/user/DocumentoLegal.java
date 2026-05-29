package com.pulse_gym.lb_common.entity.user;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoDocumentoLegal;
import com.pulse_gym.lb_common.enums.EnumTipoDocumentoLegal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "documento_legal")
public class DocumentoLegal {

    /** El ID del documento es obligatorio */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;

    /** El ID del usuario es obligatorio */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_id_usuario", nullable = false)
    private UsuarioPerfil usuario;

    /** El tipo de documento es obligatorio */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private EnumTipoDocumentoLegal tipoDocumento;

    /** La fecha de firma es obligatoria */
    @Column(name = "fecha_firma", nullable = false)
    private LocalDateTime fechaFirma;

    /** La URL del archivo firmado */
    @Column(name = "url_archivo_firmado", length = 255)
    private String urlArchivoFirmado;

    /** El estado del documento es obligatorio */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EnumEstadoDocumentoLegal estado = EnumEstadoDocumentoLegal.VIGENTE;

    /** La fecha de creación es obligatoria */
    @PrePersist
    protected void onCreate() {
        fechaFirma = LocalDateTime.now();
    }
}
