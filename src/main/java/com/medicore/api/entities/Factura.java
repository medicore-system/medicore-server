package com.medicore.api.entities;

import com.medicore.api.entities.Cita.Cita;
import com.medicore.api.entities.costos.Liquidacion;
import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa una factura generada por una atención médica en MediCore.
 * Asocia la cita, la EPS, el hospital, el servicio prestado y la liquidación correspondiente.
 */
@Entity
@Table(name = "factura")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

    /** Código único de la factura. */
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    /** Fecha de emisión de la factura. */
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    /** Indica si la factura está activa o fue anulada. */
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;

    /** Costo total de la factura. */
    @Column(name = "costo_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoTotal;

    /** Descripción del concepto facturado. */
    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    /** Cita médica que originó esta factura. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_cita", nullable = false, referencedColumnName = "codigo")
    private Cita cita;

    /** EPS del paciente asociada a esta factura. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_eps", nullable = false, referencedColumnName = "codigo")
    private Eps eps;

    /** Hospital donde se prestó el servicio. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_hospital", nullable = false, referencedColumnName = "codigo")
    private Hospital hospital;

    /** Liquidación a la que pertenece esta factura (puede ser nula si aún no se ha liquidado). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_liquidacion")
    private Liquidacion liquidacion;

    /** Servicio médico facturado. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_servicio", referencedColumnName = "codigo")
    private Servicio servicio;
}