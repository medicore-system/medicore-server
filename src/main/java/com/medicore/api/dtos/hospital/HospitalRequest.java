package com.medicore.api.dtos.hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO (Data Transfer Object) que representa la solicitud para crear
 * un nuevo hospital en el sistema MediCore.
 *
 * <p>Contiene los campos requeridos y opcionales necesarios para el
 * registro de un hospital, con sus respectivas validaciones
 * definidas mediante anotaciones de Jakarta Bean Validation.</p>
 *
 * <p>Esta clase utiliza Lombok para generar automáticamente los métodos
 * getter, setter, constructores y el patrón de diseño builder.</p>
 *
 * @author Juan Sebastián López Guzmán
 * @see HospitalResponse
 * @see HospitalUpdateRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalRequest {

    /**
     * Código único que identifica el hospital.
     *
     * <p>Este campo es obligatorio y no debe superar los 50 caracteres.</p>
     */
    @NotBlank(message = "El código del hospital es obligatorio")
    @Size(max = 50, message = "El código no debe superar los 50 caracteres")
    private String codigo;

    /**
     * Nombre del hospital.
     *
     * <p>Este campo es obligatorio y no debe superar los 50 caracteres.</p>
     */
    @NotBlank(message = "El nombre del hospital es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
    private String nombre;

    /**
     * Dirección física del hospital.
     *
     * <p>Este campo es obligatorio y no debe superar los 150 caracteres.</p>
     */
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 150, message = "La dirección no debe superar los 150 caracteres")
    private String direccion;

    /**
     * Número de teléfono de contacto del hospital.
     *
     * <p>Este campo es obligatorio y no debe superar los 20 caracteres.</p>
     */
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
    private String telefono;

    /**
     * Código de la ciudad donde se ubica el hospital.
     *
     * <p>Este campo es obligatorio.</p>
     */
    @NotBlank(message = "El código de ciudad es obligatorio")
    private String codigoCiudad;

    /**
     * Estado del hospital.
     *
     * <p>Indica si el hospital se encuentra activo o inactivo en el sistema.
     * Este campo es opcional.</p>
     */
    private Boolean estado;
}