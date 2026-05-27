package ec.com.hvq.paneles.repository;

import ec.com.hvq.paneles.dto.HospitalizacionRowDto;
import ec.com.hvq.paneles.dto.UnidadDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PanelHospitalizacionRepository {

    private static final String UNIDADES_SQL = """
            SELECT U.CD_UNID_INT, U.DS_UNID_INT
            FROM UNID_INT U
            WHERE U.SN_ATIVO = 'S'
              AND U.CD_UNID_INT NOT IN (5, 8, 17)
            """;

    private static final String TABLA_SELECT = """
            SELECT DISTINCT TO_CHAR(atencion.hr_atendimento, 'DD-MON hh24:mi') AS HORA_ATENCION,
                            atencion.CD_ATENDIMENTO,
                            cama.DS_LEITO,
                            atencion.CD_PACIENTE,
                            COALESCE((SELECT DISTINCT 'X'
                                      FROM itpre_med it, tip_esq esq, pre_med pm
                                      WHERE pm.cd_atendimento = atencion.cd_atendimento
                                        AND it.cd_pre_med = pm.cd_pre_med
                                        AND it.cd_tip_esq = esq.cd_tip_esq
                                        AND esq.CD_TIP_ESQ = 'EXI'), '-') AS EXI,
                            COALESCE((SELECT DISTINCT 'X'
                                      FROM itpre_med it, tip_esq esq, pre_med pm
                                      WHERE pm.cd_atendimento = atencion.cd_atendimento
                                        AND it.cd_pre_med = pm.cd_pre_med
                                        AND it.cd_tip_esq = esq.cd_tip_esq
                                        AND esq.CD_TIP_ESQ = 'EXL'), '-') AS EXL,
                            COALESCE((SELECT DISTINCT 'X'
                                      FROM itpre_med it, tip_esq esq, pre_med pm
                                      WHERE pm.cd_atendimento = atencion.cd_atendimento
                                        AND it.cd_pre_med = pm.cd_pre_med
                                        AND it.cd_tip_esq = esq.cd_tip_esq
                                        AND esq.CD_TIP_ESQ = 'CAP'), '-') AS CARDIO,
                            COALESCE((SELECT DISTINCT 'X'
                                      FROM itpre_med it, tip_esq esq, pre_med pm
                                      WHERE pm.cd_atendimento = atencion.cd_atendimento
                                        AND it.cd_pre_med = pm.cd_pre_med
                                        AND it.cd_tip_esq = esq.cd_tip_esq
                                        AND esq.CD_TIP_ESQ = 'FSP'), '-') AS FISIO,
                            (SELECT COUNT(pm.ds_situacao)
                             FROM PAR_MED pm
                             WHERE pm.cd_atendimento = atencion.cd_atendimento
                               AND pm.ds_situacao IN ('Em Analise', 'Solicitado')) AS INTERCONSULTA,
                            COALESCE((SELECT CASE
                                                 WHEN TP_SITUACAO IS NOT NULL
                                                     THEN DECODE(ac.TP_SITUACAO, 'G', 'AGEN.', 'R', 'REAL.', 'A', 'CON AVISO')
                                                 ELSE ' '
                                             END
                                      FROM CIRURGIA_AVISO ca, AVISO_CIRURGIA ac
                                      WHERE ac.CD_ATENDIMENTO = atencion.cd_atendimento
                                        AND ca.SN_PRINCIPAL = 'S'
                                        AND ac.cd_aviso_cirurgia = ca.cd_aviso_cirurgia
                                        AND ROWNUM = 1
                                      ORDER BY ac.DT_AVISO_CIRURGIA DESC
                                      FETCH FIRST 1 ROWS ONLY), ' ') AS CIRUGIA,
                            CASE
                                WHEN atencion.DT_PREVISTA_ALTA IS NOT NULL
                                    THEN TO_CHAR(atencion.DT_PREVISTA_ALTA, 'DD-MON')
                                WHEN atencion.DT_PREVISTA_ALTA IS NULL THEN ' '
                            END AS FECHA_ALTA,
                            CASE
                                WHEN ae.nm_prestador IS NOT NULL THEN u.cd_usuario
                                WHEN ae.nm_prestador IS NULL THEN ' '
                            END AS ENFERMERA,
                            CASE
                                WHEN atencion.hr_atendimento + 30 / 1440 <= SYSDATE THEN '-'
                                WHEN atencion.hr_atendimento + 30 / 1440 >= SYSDATE THEN 'X'
                            END AS PINTAR,
                            CASE
                                WHEN atencion.nm_usuario_alta_medica IS NOT NULL THEN 'X'
                                WHEN atencion.nm_usuario_alta_medica IS NULL THEN '-'
                            END AS PAC_ALTA
            FROM leito cama
                     JOIN atendime atencion ON cama.CD_LEITO = atencion.CD_LEITO AND atencion.hr_alta IS NULL
                     LEFT JOIN editor_custom.asignacion_enfermera ae
                               ON atencion.CD_ATENDIMENTO = ae.CD_ATENDIMIENTO
                     LEFT JOIN usuarios u ON ae.cd_prestador = u.cd_prestador
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PanelHospitalizacionRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UnidadDto> findUnidades() {
        return jdbcTemplate.query(UNIDADES_SQL, (rs, rowNum) -> new UnidadDto(
                rs.getLong("CD_UNID_INT"),
                rs.getString("DS_UNID_INT")
        ));
    }

    public List<HospitalizacionRowDto> findTablaBySector(String sector) {
        String sql = TABLA_SELECT + whereClauseForSector(sector) + " ORDER BY cama.DS_LEITO";
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (!"7".equals(sector) && !"40".equals(sector)) {
            params.addValue("unidad", sector);
        }
        return jdbcTemplate.query(sql, params, new HospitalizacionRowMapper());
    }

    private String whereClauseForSector(String sector) {
        return switch (sector) {
            case "7" -> """
                    WHERE cama.cd_unid_int IN (7, 5, 8, 17)
                      AND cama.tp_situacao IN ('A', 'O')
                    """;
            case "10" -> """
                    WHERE cama.cd_unid_int = :unidad
                      AND cama.tp_situacao IN ('A', 'O')
                      AND cama.DS_LEITO <= 'H3 333'
                    """;
            case "40" -> """
                    WHERE cama.cd_unid_int = 10
                      AND cama.tp_situacao IN ('A', 'O')
                      AND cama.DS_LEITO >= 'H3 334'
                    """;
            default -> """
                    WHERE cama.cd_unid_int = :unidad
                      AND cama.tp_situacao IN ('A', 'O')
                    """;
        };
    }

    private static final class HospitalizacionRowMapper implements RowMapper<HospitalizacionRowDto> {
        @Override
        public HospitalizacionRowDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new HospitalizacionRowDto(
                    rs.getString("HORA_ATENCION"),
                    getLong(rs, "CD_ATENDIMENTO"),
                    rs.getString("DS_LEITO"),
                    getLong(rs, "CD_PACIENTE"),
                    rs.getString("EXI"),
                    rs.getString("EXL"),
                    rs.getString("CARDIO"),
                    rs.getString("FISIO"),
                    rs.getInt("INTERCONSULTA"),
                    rs.getString("CIRUGIA"),
                    rs.getString("FECHA_ALTA"),
                    rs.getString("ENFERMERA"),
                    rs.getString("PINTAR"),
                    rs.getString("PAC_ALTA")
            );
        }

        private static Long getLong(ResultSet rs, String column) throws SQLException {
            long value = rs.getLong(column);
            return rs.wasNull() ? null : value;
        }
    }
}
