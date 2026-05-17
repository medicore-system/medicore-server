package com.medicore.api.entities.costos;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Factura;

@Entity
@Table(name = "liquidacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Liquidacion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_eps", nullable = false)
  private Eps eps;

  @Column(name = "fecha_inicio", nullable = false)
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin", nullable = false)
  private LocalDate fechaFin;

  @Column(name = "total_bruto", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalBruto;

  @Column(name = "total_cobertura_eps", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalCoberturaEps;

  @Column(name = "total_copago_paciente", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalCopagoPaciente;

  @Column(nullable = false, length = 20)
  @Builder.Default
  private String estado = "PENDIENTE";

  @Column(name = "fecha_generacion", insertable = false, updatable = false)
  private LocalDateTime fechaGeneracion;

  @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
  @Builder.Default
  private List<Factura> facturas = new ArrayList<>();
}