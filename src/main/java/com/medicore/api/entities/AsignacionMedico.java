package com.medicore.api.entities;

import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidad que representa la asignación de un médico a un hospital en una ciudad y período determinados.
 * Incluye el horario asignado y el estado activo/inactivo de la asignación.
 */
@Entity
@Table(name = "asignacion_medico")
@Data
@NoArgsConstructor @AllArgsConstructor
public class AsignacionMedico {

    /** Identificador autogenerado de la asignación. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    /** Fecha de inicio del período de asignación. */
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    /** Fecha de fin del período de asignación. */
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    /** Médico asignado. */
    @ManyToOne
    @JoinColumn(name = "documento_medico", nullable = false)
    private Medico medico;

    /** Hospital al que se asigna el médico. */
    @ManyToOne
    @JoinColumn(name = "codigo_hospital", nullable = false)
    private Hospital hospital;

    /** Ciudad donde se realiza la asignación. */
    @ManyToOne
    @JoinColumn(name = "codigo_ciudad",  nullable = false)
    private Ciudad ciudad;

    /** Horario de trabajo asignado al médico. */
    @ManyToOne
    @JoinColumn(name = "codigo_horario", nullable = false)
    private HorarioMedico horario;

    /** Indica si la asignación está activa. */
    @Column(name = "estado",  nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado = true;
}
