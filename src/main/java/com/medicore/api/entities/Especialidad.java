package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Entidad que representa una especialidad médica
 * dentro del sistema.
 *
 * <p>
 * Una especialidad puede estar asociada
 * a múltiples médicos y citas.
 * </p>
 */
@Entity
@Table(name = "especialidad")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "medico")
public class Especialidad {

    /**
     * Identificador único de la especialidad.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de la especialidad médica.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * Lista de médicos asociados a la especialidad.
     */
    @OneToMany(mappedBy = "especialidad")
    private List<Medico> medico;

    /**
     * Lista de citas asociadas a la especialidad.
     */
    @OneToMany(mappedBy = "especialidad")
    private List<Cita> citas;
}