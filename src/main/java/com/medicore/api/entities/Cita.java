package com.medicore.api.entities;

import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * Entidad que representa una cita médica dentro del sistema.
 *
 * <p>Una cita relaciona un usuario/paciente,
 * un médico, un hospital y un tipo de cita.</p>
 *
 * <p>Además almacena información relacionada con:
 * <ul>
 *     <li>Fecha</li>
 *     <li>Hora</li>
 *     <li>Costo</li>
 *     <li>Estado de la cita</li>
 * </ul>
 * </p>
 *
 * @author Manuel
 */
@Entity
@Table(name = "cita")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Cita {

    /**
     * Código único identificador de la cita.
     */
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    /**
     * Estado actual de la cita.
     *
     * <p>Valores posibles:
     * <ul>
     *     <li>APROBADA</li>
     *     <li>PENDIENTE</li>
     *     <li>DENEGADA</li>
     * </ul>
     * </p>
     *
     * <p>Por defecto el estado es "PENDIENTE".</p>
     */
    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "PENDIENTE";

    /**
     * Fecha programada de la cita médica.
     */
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    /**
     * Valor monetario asociado a la cita.
     */
    @Column(name = "costo",  nullable = false)
    private BigDecimal costo;

    /**
     * Tipo de cita asociado.
     *
     * <p>Representa la categoría o clasificación
     * de la cita médica.</p>
     */
    @ManyToOne
    @JoinColumn(name = "id_tipo", nullable = false)
    private Especialidad especialidad;

    /**
     * Usuario/paciente asociado a la cita.
     */
    @OneToOne
    @JoinColumn(name = "documento_paciente", nullable = false)
    private Usuario usuario;

    /**
     * Documento del médico encargado de atender la cita.
     */
    @ManyToOne
    @JoinColumn(name = "documento_medico", nullable = false)
    private Medico medico;

    /**
     * Hospital donde se realizará la cita médica.
     *
     * <p>Muchos registros de citas pueden pertenecer
     * a un mismo hospital.</p>
     */
    @ManyToOne
    @JoinColumn(name = "codigo_hospital", nullable = false)
    private Hospital  hospital;
}
