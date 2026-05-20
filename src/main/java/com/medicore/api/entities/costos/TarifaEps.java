package com.medicore.api.entities.costos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Servicio;

/**
 * Entidad que representa la tarifa de cobertura de una EPS para un servicio médico específico.
 * Define el porcentaje que la EPS cubre del costo del servicio.
 */
@Entity
@Table(name = "tarifa_eps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaEps {

    /** Código único de la tarifa, generado con prefijo "TAEPS". */
    @Id
    @GeneratedValue(generator = "tarifa_seq")
    @GenericGenerator(
        name = "tarifa_seq",
        strategy = "com.medicore.api.util.PrefixedIdGenerator",
        parameters = {
            @Parameter(name = "prefix", value = "TAEPS"),
            @Parameter(name = "sequence", value = "seq_tarifa_eps")
        }
    )
    @Column(name = "codigo", length = 50)
    private String codigo;

    /** EPS a la que aplica esta tarifa. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_eps", nullable = false)
    private Eps eps;

    /** Servicio médico al que aplica esta tarifa de cobertura. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_servicio", nullable = false)
    private Servicio servicio;

    /** Porcentaje del costo del servicio cubierto por la EPS (0 a 100). */
    @Column(name = "porcentaje_cobertura", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeCobertura;

    /** Indica si la tarifa está activa. */
    @Column(nullable = false)
    @Builder.Default
    private Boolean estado = true;

    /** Fecha y hora de creación de la tarifa (gestionada por la base de datos). */
    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}