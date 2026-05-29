package com.pulse_gym.lb_common.dto;

import com.pulse_gym.lb_common.enums.EnumTipoDocumentoLegal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentoLegalRequestDTO {

    /** El ID del usuario es obligatorio */
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    /** El tipo de documento es obligatorio */
    @NotNull(message = "El tipo de documento es obligatorio")
    private EnumTipoDocumentoLegal tipoDocumento;

    /** La URL del archivo firmado es opcional */
    private String urlArchivoFirmado;
}