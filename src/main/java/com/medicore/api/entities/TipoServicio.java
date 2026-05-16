package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un tipo de servicio
 * dentro del sistema.
 *
 * <p>
 * Define la clasificación de un servicio
 * mediante un nombre y un prefijo único.
 * </p>
 */
@Entity
@Table(name = "tipo_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoServicio {

    /**
     * Identificador único del tipo de servicio.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del tipo de servicio.
     */
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    /**
     * Prefijo identificador del tipo de servicio.
     */
    @Column(name = "prefijo", nullable = false, unique = true, length = 10)
    private String prefijo;
}