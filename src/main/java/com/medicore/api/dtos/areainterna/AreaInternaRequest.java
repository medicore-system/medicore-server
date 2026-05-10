package com.medicore.api.dtos.areainterna;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) que representa la solicitud para crear
 * una nueva área interna en el sistema MediCore.
 *
 * <p>Contiene los campos requeridos y opcionales necesarios para el
 * registro de un área interna, con sus respectivas validaciones
 * definidas mediante anotaciones de Jakarta Bean Validation.</p>
 *
 * <p>Esta clase utiliza Lombok para generar automáticamente los métodos
 * getter, setter, constructores y el patrón de diseño builder.</p>
 *
 * @author Cristian Camilo Salazar Arenas
 * @see AreaInternaResponse
 * @see AreaInternaUpdateRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaInternaRequest {

    /**
     * Código único que identifica el área interna.
     *
     * <p>Este campo es obligatorio y no debe superar los 50 caracteres.</p>
     */
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no debe superar los 50 caracteres")
    private String codigo;

    /**
     * Nombre descriptivo del área interna.
     *
     * <p>Este campo es obligatorio y no debe superar los 100 caracteres.</p>
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    private String nombre;

    /**
     * Descripción detallada del área interna.
     *
     * <p>Este campo es opcional y no debe superar los 250 caracteres.</p>
     */
    @Size(max = 250, message = "La descripción no debe superar los 250 caracteres")
    private String descripcion;

    /**
     * Código alternativo del área interna para referencias externas.
     *
     * <p>Este campo es obligatorio.</p>
     */
    @NotBlank(message = "El código de área interna es obligatorio")
    private String codigoAreaInterna;
}