package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa el historial clínico de un paciente en MediCore.
 * Agrupa los servicios médicos prestados en una consulta o atención.
 */
@Entity
@Table(name = "historial_clinico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialClinico {

    /** Código único del historial clínico. */
    @Id
    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    /** Fecha en que se registró el historial clínico. */
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    /** Tipo de atención médica registrada. */
    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    /** Descripción general de la atención médica. */
    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    /** Paciente al que pertenece este historial. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_paciente", nullable = false)
    private Usuario paciente;

    /** Médico que realizó la atención. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_medico", nullable = false)
    private Medico medico;

    /** Lista de servicios médicos prestados en esta atención. */
    @OneToMany(mappedBy = "historialClinico", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Servicio> servicios = new ArrayList<>();
}