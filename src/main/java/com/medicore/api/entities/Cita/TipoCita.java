package com.medicore.api.entities.Cita;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidad que representa un tipo de cita médica (ej: consulta general, urgencias, especializada).
 * Clasifica las citas dentro del sistema MediCore.
 */
@Entity
@Table(name = "tipo_cita")
@Data
@AllArgsConstructor @NoArgsConstructor
public class TipoCita {

    /** Identificador autogenerado del tipo de cita. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nombre descriptivo del tipo de cita. */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /** Lista de citas que pertenecen a este tipo. */
    @OneToMany(mappedBy = "tipoCita")
    private List<Cita> cita;
}
