package com.pulse_gym.ms_users.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pulse_gym.lb_common.entity.user.DocumentoLegal;
import com.pulse_gym.lb_common.enums.EnumEstadoDocumentoLegal;
import com.pulse_gym.lb_common.enums.EnumTipoDocumentoLegal;

import feign.Param;

public interface DocumentoLegalRepository extends JpaRepository<DocumentoLegal, Long> {

    List<DocumentoLegal> findByFkIdUsuarioAndEstado(Long fkIdUsuario, EnumEstadoDocumentoLegal estado);

    Optional<DocumentoLegal> findByIdDocumentoAndEstado(Long idDocumento, EnumEstadoDocumentoLegal estado);

    @Query("SELECT d FROM DocumentoLegal d WHERE d.fkIdUsuario = :idUsuario AND d.tipoDocumento = :tipo AND d.estado = :estado")
    Optional<DocumentoLegal> findDocumentoPorTipo(
            @Param("idUsuario") Long idUsuario,
            @Param("tipo") EnumTipoDocumentoLegal tipo,
            @Param("estado") EnumEstadoDocumentoLegal estado);
}
