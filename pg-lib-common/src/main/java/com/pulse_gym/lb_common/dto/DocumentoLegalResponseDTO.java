package com.pulse_gym.lb_common.dto;

import java.time.LocalDateTime;

import com.pulse_gym.lb_common.enums.EnumEstadoDocumentoLegal;
import com.pulse_gym.lb_common.enums.EnumTipoDocumentoLegal;

import lombok.Data;

@Data
public class DocumentoLegalResponseDTO {

    /** El ID del documento es obligatorio */
    private Long idDocumento;

    /** El ID del usuario es obligatorio */
    private Long idUsuario;

    /** El nombre del usuario es obligatorio */
    private String nombreUsuario;

    /** El tipo de documento es obligatorio */
    private EnumTipoDocumentoLegal tipoDocumento;

    /** La fecha de firma es obligatoria */
    private LocalDateTime fechaFirma;

    /** La URL del archivo firmado es obligatoria */
    private String urlArchivoFirmado;

    /** El estado del documento es obligatorio */
    private EnumEstadoDocumentoLegal estado;
}