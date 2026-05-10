package com.medicore.api.dtos.areainterna;

import lombok.*;

/**
 * DTO (Data Transfer Object) que representa la respuesta con la
 * información de un área interna en el sistema MediCore.
 *
 * <p>Esta clase se utiliza para devolver los datos de un área interna
 * desde el servidor hacia el cliente, incluyendo tanto los campos
 * de identificación como los descriptivos.</p>
 *
 * <p>Utiliza Lombok para generar automáticamente los métodos
 * getter, setter, constructores y el patrón de diseño builder.</p>
 *
 * @author Juan Sebastián López Guzmán
 * @see AreaInternaRequest
 * @see AreaInternaUpdateRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaInternaResponse {

    /**
     * Código único que identifica el área interna.
     */
    private String codigo;

    /**
     * Nombre descriptivo del área interna.
     */
    private String nombre;

    /**
     * Descripción detallada del área interna.
     */
    private String descripcion;

    /**
     * Código alternativo del área interna para referencias externas.
     */
    private String codigoAreaInterna;

    /**
     * Nombre completo del área interna utilizado para visualización.
     */
    private String nombreAreaInterna;
}