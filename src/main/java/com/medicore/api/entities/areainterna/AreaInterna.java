package com.medicore.api.entities.areainterna;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa un área interna en el sistema MediCore.
 *
 * <p>Esta clase mapea la tabla {@code area_interna} de la base de datos
 * y contiene la información básica de identificación y descripción
 * de cada área interna registrada en el sistema.</p>
 *
 * <p>Utiliza Lombok para generar automáticamente los métodos
 * getter, setter y el constructor sin argumentos requerido por JPA.</p>
 *
 * @author Juan Sebastián López Guzmán
 */
@Entity
@Table(name = "area_interna")
@Getter
@Setter
@NoArgsConstructor
public class AreaInterna {

    /**
     * Código único que identifica el área interna.
     *
     * <p>Actúa como clave primaria de la entidad. Su valor es asignado
     * manualmente y tiene una longitud máxima de 50 caracteres.</p>
     */
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    /**
     * Nombre descriptivo del área interna.
     *
     * <p>Este campo es obligatorio y tiene una longitud máxima de
     * 50 caracteres.</p>
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}