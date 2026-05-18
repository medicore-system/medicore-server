package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Entidad que representa un médico dentro del sistema.
 *
 * <p>
 * Contiene la información personal del médico,
 * su especialidad, ciudad asociada y las citas
 * relacionadas.
 * </p>
 */
@Entity
@Table (name = "medico")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "especialidad")
public class Medico {

    /**
     * Documento de identificación del médico.
     */
    @Id
    @Column(name = "documento")
    private String documento;

    /**
     * Nombre del médico.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * Apellido del médico.
     */
    @Column(name = "apellido")
    private String apellido;

    /**
     * Número telefónico del médico.
     */
    @Column(name =  "telefono")
    private String telefono;

    /**
     * Correo electrónico del médico.
     */
    @Column(name = "correo")
    private String email;

    /**
     * Estado del médico dentro del sistema.
     */
    @Column(name = "estado", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado;

    /**
     * Especialidad asociada al médico.
     */
    @ManyToOne
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;

    /**
     * Ciudad asociada al médico.
     */
    @OneToOne
    @JoinColumn(name = "codigo_ciudad")
    private Ciudad ciudad;

    /**
     * Lista de citas asignadas al médico.
     */
    @OneToMany(mappedBy = "medico")
    private List<Cita> citas;

    /**
     * Lista de asignaciones de un médico.
     */
    @OneToMany(mappedBy = "medico")
    private List<AsignacionMedico> asiganciones;
}