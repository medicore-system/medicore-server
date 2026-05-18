package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    @Id
    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    @Column(name = "procedimiento", length = 250)
    private String procedimiento;

    @Column(name = "resultados", length = 250)
    private String resultados;

    @Column(name = "costo", nullable = false, precision = 12, scale = 2)
    private BigDecimal costo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;

    @Column(name = "codigo_historial", length = 50)
    private String codigoHistorial;

    @Column(name = "estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;
}