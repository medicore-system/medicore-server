package com.medicore.api.dtos.areainterna;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) que representa la solicitud para actualizar
 * la información de un área interna existente en el sistema MediCore.
 *
 * <p>Contiene los campos que pueden ser modificados en un área interna,
 * con sus respectivas validaciones definidas mediante anotaciones de
 * Jakarta Bean Validation. A diferencia de {@link AreaInternaRequest},
 * esta clase no incluye el campo {@code codigo}, ya que el identificador
 * del área a actualizar se proporciona como parámetro de ruta en la
 * petición.</p>
 *
 * <p>Esta clase utiliza Lombok para generar automáticamente los métodos
 * getter, setter, constructores y el patrón de diseño builder.</p>
 *
 * @author Cristian Camilo Salazar Arenas
 * @see AreaInternaRequest
 * @see AreaInternaResponse
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaInternaUpdateRequest {

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