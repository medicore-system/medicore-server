package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Entidad que representa un tipo de cita médica.
 *
 * <p>Permite clasificar las citas según su naturaleza
 * o propósito dentro del sistema.</p>
 *
 * <p>Ejemplos:
 * <ul>
 *     <li>Consulta general</li>
 *     <li>Control médico</li>
 *     <li>Urgencia</li>
 *     <li>Especializada</li>
 * </ul>
 * </p>
 *
 * @author Manuel
 */
@Entity
@Table(name = "tipo_cita")
@Data
@AllArgsConstructor @NoArgsConstructor
public class TipoCita {

    /**
     * Identificador único del tipo de cita.
     *
     * <p>El valor es generado automáticamente
     * por la base de datos.</p>
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del tipo de cita.
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Lista de citas asociadas a este tipo de cita.
     *
     * <p>Representa la relación uno a muchos entre
     * un tipo de cita y las citas registradas.</p>
     */
    @OneToMany(mappedBy = "tipoCita")
    private List<Cita> citas;

}
