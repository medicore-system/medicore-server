package com.medicore.api.entities.costos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Servicio;

@Entity
@Table(name = "tarifa_eps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TarifaEps {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_eps", nullable = false)
    private Eps eps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_servicio", nullable = false)
    private Servicio servicio;

    @Column(name = "porcentaje_cobertura", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeCobertura;

    @Column(nullable = false)
    @Builder.Default
    private Boolean estado = true;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;
}