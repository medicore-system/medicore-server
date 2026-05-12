package com.medicore.api.repositories;

import com.medicore.api.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, String> {

    List<Servicio> findAllByOrderByCodigoAsc();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndCodigoNot(String nombre, String codigo);

    @Query("""
            SELECT s
            FROM Servicio s
            WHERE LOWER(s.codigo) LIKE LOWER(CONCAT('%', :termino, '%'))
               OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
               OR LOWER(s.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
            ORDER BY s.codigo ASC
            """)
    List<Servicio> buscarPorTermino(@Param("termino") String termino);

    @Query(value = """
            SELECT COALESCE(MAX(CAST(SUBSTRING(codigo FROM LENGTH(:prefijo) + 2) AS INTEGER)), 0)
            FROM servicio
            WHERE codigo ~ CONCAT('^', :prefijo, '-[0-9]+$')
            """, nativeQuery = true)
    int findMaxCodigoSequenceByPrefijo(@Param("prefijo") String prefijo);

    @Query(value = """
            SELECT COUNT(*) > 0
            FROM historial_clinico
            WHERE codigo = :codigoHistorial
            """, nativeQuery = true)
    boolean existsHistorialClinicoByCodigo(@Param("codigoHistorial") String codigoHistorial);

    @Query(value = """
            SELECT
                h.codigo AS codigo,
                h.fecha AS fecha,
                h.tipo AS tipo,
                h.descripcion AS descripcion,
                u.documento AS "documentoPaciente",
                CONCAT(u.nombre, ' ', u.apellido) AS "nombrePaciente",
                m.documento AS "documentoMedico",
                CONCAT(m.nombre, ' ', m.apellido) AS "nombreMedico"
            FROM servicio s
            JOIN historial_clinico h ON h.codigo = s.codigo_historial
            LEFT JOIN usuario u ON u.documento = h.documento_paciente
            LEFT JOIN medico m ON m.documento = h.documento_medico
            WHERE s.codigo = :codigoServicio
            """, nativeQuery = true)
    Optional<HistorialClinicoResumenProjection> findHistorialByServicioCodigo(
            @Param("codigoServicio") String codigoServicio
    );

    @Query(value = """
            SELECT
                f.codigo AS codigo,
                f.fecha AS fecha,
                f.estado AS estado,
                f.costo_total AS "costoTotal",
                f.descripcion AS descripcion,
                f.codigo_cita AS "codigoCita",
                f.codigo_eps AS "codigoEps",
                e.nombre AS "nombreEps",
                f.codigo_hospital AS "codigoHospital",
                h.nombre AS "nombreHospital"
            FROM factura f
            LEFT JOIN eps e ON e.codigo = f.codigo_eps
            LEFT JOIN hospital h ON h.codigo = f.codigo_hospital
            WHERE f.codigo_servicio = :codigoServicio
            ORDER BY f.fecha DESC, f.codigo ASC
            """, nativeQuery = true)
    List<FacturaServicioProjection> findFacturasByServicioCodigo(
            @Param("codigoServicio") String codigoServicio
    );

    interface HistorialClinicoResumenProjection {
        String getCodigo();
        LocalDate getFecha();
        String getTipo();
        String getDescripcion();
        String getDocumentoPaciente();
        String getNombrePaciente();
        String getDocumentoMedico();
        String getNombreMedico();
    }

    interface FacturaServicioProjection {
        String getCodigo();
        LocalDate getFecha();
        Boolean getEstado();
        BigDecimal getCostoTotal();
        String getDescripcion();
        String getCodigoCita();
        String getCodigoEps();
        String getNombreEps();
        String getCodigoHospital();
        String getNombreHospital();
    }
}