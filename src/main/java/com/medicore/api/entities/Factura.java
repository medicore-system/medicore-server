package com.medicore.api.entities;

import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.medicore.api.entities.costos.Liquidacion;

@Entity
@Table(name = "factura")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;

    @Column(name = "costo_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_cita", nullable = false, referencedColumnName = "codigo")
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_eps", nullable = false, referencedColumnName = "codigo")
    private Eps eps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_hospital", nullable = false, referencedColumnName = "codigo")
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_liquidacion")
    private Liquidacion liquidacion;
}