package com.medicore.api.dtos.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para recibir la información
 * necesaria para actualizar un médico existente.
 *
 * <p>
 * Contiene únicamente los datos editables
 * del médico dentro del sistema.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicoUpdateDTO {

    /**
     * Nuevo nombre del médico.
     */
    private String nombre;

    /**
     * Nuevo apellido del médico.
     */
    private String apellido;

    /**
     * Identificador de la especialidad médica.
     */
    private Integer idEspecialidad;

    /**
     * Nuevo número telefónico del médico.
     */
    private String telefono;

    /**
     * Nuevo correo electrónico del médico.
     */
    private String email;

    /**
     * Código de la ciudad asociada al médico.
     */
    private String codigoCiudad;
}