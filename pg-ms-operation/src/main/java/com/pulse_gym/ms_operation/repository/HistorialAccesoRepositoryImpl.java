package com.pulse_gym.ms_operation.repository;

import com.pulse_gym.lb_common.dto.HistorialAccesoDTO;
import com.pulse_gym.lb_common.dto.HistorialAccesoFiltroDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HistorialAccesoRepositoryImpl implements HistorialAccesoRepositoryCustom {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Page<HistorialAccesoDTO> consultarHistorialAccesos(HistorialAccesoFiltroDTO filtro, Pageable pageable) {
        String sql = buildQuery(filtro, true);
        String countSql = buildQuery(filtro, false);

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("usuarioId", filtro.getUsuarioId());
        params.addValue("fechaInicio", filtro.getFechaInicio() != null ? filtro.getFechaInicio() : LocalDateTime.MIN);
        params.addValue("fechaFin", filtro.getFechaFin() != null ? filtro.getFechaFin() : LocalDateTime.MAX);

        String tipoAcceso = filtro.getTipoAcceso();
        if (tipoAcceso != null && tipoAcceso.equalsIgnoreCase("WEB")) {
            params.addValue("tipoAcceso", "WEB");
        } else if (tipoAcceso != null && tipoAcceso.equalsIgnoreCase("HUELLA")) {
            params.addValue("tipoAcceso", "BIOMETRICO");
        } else {
            params.addValue("tipoAcceso", null);
        }

        params.addValue("resultado", filtro.getResultado());

        params.addValue("offset", pageable.getOffset());
        params.addValue("limit", pageable.getPageSize());

        List<HistorialAccesoDTO> content = jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<>(HistorialAccesoDTO.class));

        Long total = jdbcTemplate.queryForObject(countSql, params, Long.class);

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    private String buildQuery(HistorialAccesoFiltroDTO filtro, boolean withPagination) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("    a.id_usuario AS usuarioId, ");
        sql.append("    CONCAT(u.nombre, ' ', u.apellido) AS nombreUsuario, ");
        sql.append("    a.fecha_hora_entrada AS fechaHora, ");
        sql.append("    a.tipo_acceso AS tipoAcceso, ");
        sql.append("    CASE ");
        sql.append("        WHEN a.estado_acceso = 'PERMITIDO' THEN 'EXITOSO' ");
        sql.append("        WHEN a.estado_acceso = 'DENEGADO' THEN 'FALLIDO' ");
        sql.append("        ELSE 'DESCONOCIDO' ");
        sql.append("    END AS resultado, ");
        sql.append("    a.motivo_denegacion AS motivo, ");
        sql.append("    a.id_sede AS sedeId, ");
        sql.append("    s.nombre_sede AS nombreSede ");
        sql.append("FROM operations_schema.asistencia a ");
        sql.append("LEFT JOIN users_schema.usuario_perfil u ON a.id_usuario = u.id_usuario ");
        sql.append("LEFT JOIN operations_schema.sede s ON a.id_sede = s.id_sede ");
        sql.append("WHERE 1=1 ");
        sql.append("  AND (:usuarioId IS NULL OR a.id_usuario = :usuarioId) ");
        sql.append("  AND a.fecha_hora_entrada BETWEEN :fechaInicio AND :fechaFin ");
        sql.append("  AND ( ");
        sql.append("        (:tipoAcceso IS NULL) ");
        sql.append("        OR (:tipoAcceso = 'WEB' AND a.tipo_acceso IN ('WEB', 'APP')) ");
        sql.append("        OR (:tipoAcceso = 'BIOMETRICO' AND a.tipo_acceso = 'BIOMETRICO') ");
        sql.append("      ) ");
        sql.append("  AND ( ");
        sql.append("        (:resultado IS NULL) ");
        sql.append("        OR (:resultado = 'EXITOSO' AND a.estado_acceso = 'PERMITIDO') ");
        sql.append("        OR (:resultado = 'FALLIDO' AND a.estado_acceso = 'DENEGADO') ");
        sql.append("        OR (:resultado = 'BLOQUEADO' AND 1=0) "); 
        sql.append("      ) ");

        sql.append("UNION ALL ");
        sql.append("SELECT ");
        sql.append("    ab.id_usuario AS usuarioId, ");
        sql.append("    CONCAT(u.nombre, ' ', u.apellido) AS nombreUsuario, ");
        sql.append("    ab.fecha_hora AS fechaHora, ");
        sql.append("    'BIOMETRICO' AS tipoAcceso, ");
        sql.append("    CASE ");
        sql.append("        WHEN ab.exitoso = true THEN 'EXITOSO' ");
        sql.append("        WHEN ab.exitoso = false AND ab.mensaje LIKE '%bloqueado%' THEN 'BLOQUEADO' ");
        sql.append("        ELSE 'FALLIDO' ");
        sql.append("    END AS resultado, ");
        sql.append("    ab.mensaje AS motivo, ");
        sql.append("    ab.id_sede AS sedeId, ");
        sql.append("    s.nombre_sede AS nombreSede ");
        sql.append("FROM operations_schema.auditoria_biometrica ab ");
        sql.append("LEFT JOIN users_schema.usuario_perfil u ON ab.id_usuario = u.id_usuario ");
        sql.append("LEFT JOIN operations_schema.sede s ON ab.id_sede = s.id_sede ");
        sql.append("WHERE 1=1 ");
        sql.append("  AND (:usuarioId IS NULL OR ab.id_usuario = :usuarioId) ");
        sql.append("  AND ab.fecha_hora BETWEEN :fechaInicio AND :fechaFin ");
        
        sql.append("  AND (:tipoAcceso IS NULL OR :tipoAcceso = 'BIOMETRICO') ");
        // Filtro por resultado
        sql.append("  AND ( ");
        sql.append("        (:resultado IS NULL) ");
        sql.append("        OR (:resultado = 'EXITOSO' AND ab.exitoso = true) ");
        sql.append("        OR (:resultado = 'FALLIDO' AND ab.exitoso = false AND ab.mensaje NOT LIKE '%bloqueado%') ");
        sql.append("        OR (:resultado = 'BLOQUEADO' AND ab.exitoso = false AND ab.mensaje LIKE '%bloqueado%') ");
        sql.append("      ) ");

        // Orden y paginación
        sql.append("ORDER BY fechaHora DESC ");
        if (withPagination) {
            sql.append("OFFSET :offset LIMIT :limit ");
        }

        return sql.toString();
    }
}