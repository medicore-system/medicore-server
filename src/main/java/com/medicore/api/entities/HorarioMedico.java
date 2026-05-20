package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad que representa un turno de horario médico en MediCore.
 * Define la franja horaria de trabajo y el siguiente horario en la rotación.
 */
@Entity
@Table(name = "horario_medico")
@Data
@AllArgsConstructor @NoArgsConstructor
public class HorarioMedico {

    /** Identificador autogenerado del horario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    /** Descripción del horario de trabajo (ej: "MAÑANA", "TARDE", "NOCHE"). */
    @Column(name = "horario", nullable = false)
    private String horario;

    /** Siguiente horario en la secuencia de rotación del médico. */
    @Column(name = "siguiente_horario",  nullable = false)
    private String siguienteHorario;

    /** Lista de asignaciones médicas que usan este horario. */
    @OneToMany(mappedBy = "horario")
    private List<AsignacionMedico> asignacionMedicos;
}
