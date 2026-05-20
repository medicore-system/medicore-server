package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un departamento geográfico de Colombia.
 * Sirve como agrupador de ciudades en la jerarquía territorial del sistema.
 */
@Entity
@Table(name = "departamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departamento {

    /** Identificador autogenerado del departamento. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nombre único del departamento. */
    @Column(name = "nombre", nullable = false, length = 100, unique = true)
    private String nombre;
}
