package com.medicore.api.dtos.hospital;

import lombok.*;

/**
 * DTO (Data Transfer Object) que representa la respuesta con la
 * información de un hospital en el sistema MediCore.
 *
 * <p>Esta clase se utiliza para devolver los datos de un hospital
 * desde el servidor hacia el cliente, incluyendo tanto los campos
 * de identificación y contacto como la información de la ciudad
 * donde se ubica.</p>
 *
 * <p>Utiliza Lombok para generar automáticamente los métodos
 * getter, setter, constructores y el patrón de diseño builder.</p>
 *
 * @author Juan Sebastián López Guzmán
 * @see HospitalRequest
 * @see HospitalUpdateRequest
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {

    /**
     * Código único que identifica el hospital.
     */
    private String codigo;

    /**
     * Nombre del hospital.
     */
    private String nombre;

    /**
     * Dirección física del hospital.
     */
    private String direccion;

    /**
     * Número de teléfono de contacto del hospital.
     */
    private String telefono;

    /**
     * Estado del hospital.
     *
     * <p>Indica si el hospital se encuentra activo ({@code true})
     * o inactivo ({@code false}) en el sistema.</p>
     */
    private Boolean estado;

    /**
     * Código de la ciudad donde se ubica el hospital.
     */
    private String codigoCiudad;

    /**
     * Nombre de la ciudad donde se ubica el hospital.
     */
    private String nombreCiudad;
}