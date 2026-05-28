package com.pulse_gym.lb_common.entity.user;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoDocumentoLegal;
import com.pulse_gym.lb_common.enums.EnumTipoDocumentoLegal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "documento_legal")
public class DocumentoLegal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;

    @Column(name = "fk_id_usuario", nullable = false)
    private Long fkIdUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private EnumTipoDocumentoLegal tipoDocumento;

    @Column(name = "fecha_firma", nullable = false)
    private LocalDateTime fechaFirma;

    @Column(name = "url_archivo_firmado", length = 255)
    private String urlArchivoFirmado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EnumEstadoDocumentoLegal estado = EnumEstadoDocumentoLegal.Vigente;

    @PrePersist
    protected void onCreate() {
        fechaFirma = LocalDateTime.now();
    }
}
