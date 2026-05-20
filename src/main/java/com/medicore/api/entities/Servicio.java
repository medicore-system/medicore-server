package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un servicio médico ofrecido en MediCore.
 * Puede estar asociado a un historial clínico y generar facturas.
 */
@Entity
@Table(name = "servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    /** Código único del servicio médico. */
    @Id
    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    /** Nombre del servicio médico. */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /** Descripción general del servicio. */
    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    /** Procedimiento médico asociado al servicio. */
    @Column(name = "procedimiento", length = 250)
    private String procedimiento;

    /** Resultados esperados o entregados del servicio. */
    @Column(name = "resultados", length = 250)
    private String resultados;

    /** Costo base del servicio médico. */
    @Column(name = "costo", nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    /** Tipo de servicio al que pertenece. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    /** Historial clínico al que está asociado este servicio (puede ser nulo). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_historial")
    private HistorialClinico historialClinico;

    /** Facturas generadas por este servicio. */
    @OneToMany(mappedBy = "servicio", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Factura> facturas = new ArrayList<>();

    /** Indica si el servicio está activo en el catálogo. */
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}