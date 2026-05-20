package com.medicore.api.entities;

import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entidad que representa una ciudad dentro de un departamento en Colombia.
 * Agrupa hospitales, pacientes y asignaciones médicas a nivel territorial.
 */
@Entity
@Table(name = "ciudad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ciudad {

    /** Código único de la ciudad (DANE o interno). */
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    /** Nombre de la ciudad. */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /** Departamento al que pertenece la ciudad. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento", nullable = false)
    private Departamento departamento;

    /** Indica si la ciudad está activa en el sistema. */
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;

    /** Lista de usuarios (pacientes) registrados en esta ciudad. */
    @OneToMany(mappedBy = "ciudad")
    private List<Usuario> pacientes;

    /** Lista de hospitales ubicados en esta ciudad. */
    @OneToMany(mappedBy = "ciudad")
    private List<Hospital> hospitales;

    /** Lista de asignaciones médicas realizadas en esta ciudad. */
    @OneToMany(mappedBy = "ciudad")
    private List<AsignacionMedico> asignacionMedicos;
}
