package com.medicore.api.entities.costos;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Factura;

/**
 * Entidad que representa una liquidación de cuentas entre MediCore y una EPS.
 * Consolida el total bruto, la cobertura de la EPS y el copago del paciente en un período dado.
 */
@Entity
@Table(name = "liquidacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Liquidacion {

  /** Código único de la liquidación, generado con prefijo "LIQ". */
  @Id
  @GeneratedValue(generator = "liquidacion_seq")
  @GenericGenerator(name = "liquidacion_seq", strategy = "com.medicore.api.util.PrefixedIdGenerator", parameters = {
      @Parameter(name = "prefix", value = "LIQ"),
      @Parameter(name = "sequence", value = "seq_liquidacion")
  })
  @Column(name = "codigo", length = 50)
  private String codigo;

  /** EPS a la que corresponde esta liquidación. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "codigo_eps", nullable = false)
  private Eps eps;

  /** Fecha de inicio del período liquidado. */
  @Column(name = "fecha_inicio", nullable = false)
  private LocalDate fechaInicio;

  /** Fecha de fin del período liquidado. */
  @Column(name = "fecha_fin", nullable = false)
  private LocalDate fechaFin;

  /** Total bruto de los servicios prestados en el período. */
  @Column(name = "total_bruto", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalBruto;

  /** Monto cubierto por la EPS. */
  @Column(name = "total_cobertura_eps", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalCoberturaEps;

  /** Monto a cargo del paciente (copago). */
  @Column(name = "total_copago_paciente", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalCopagoPaciente;

  /** Estado de la liquidación (PENDIENTE, APROBADA, PAGADA, etc.). */
  @Column(nullable = false, length = 20)
  @Builder.Default
  private String estado = "PENDIENTE";

  /** Fecha y hora en que se generó la liquidación (gestionada por la base de datos). */
  @Column(name = "fecha_generacion", insertable = false, updatable = false)
  private LocalDateTime fechaGeneracion;

  /** Facturas asociadas a esta liquidación. */
  @OneToMany(mappedBy = "liquidacion", fetch = FetchType.LAZY)
  @Builder.Default
  private List<Factura> facturas = new ArrayList<>();
}