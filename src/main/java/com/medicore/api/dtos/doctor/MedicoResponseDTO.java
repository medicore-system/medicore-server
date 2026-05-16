package com.medicore.api.dtos.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para enviar al frontend
 * la información de un médico.
 *
 * <p>
 * Contiene los datos visibles y necesarios
 * para representar un médico dentro del sistema.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponseDTO {

    /**
     * Documento de identificación del médico.
     */
    private String documento;

    /**
     * Nombre del médico.
     */
    private String nombre;

    /**
     * Apellido del médico.
     */
    private String apellido;

    /**
     * Nombre de la especialidad médica.
     */
    private String NombreEspecialidad;

    /**
     * Número telefónico del médico.
     */
    private String telefono;

    /**
     * Correo electrónico del médico.
     */
    private String email;

    /**
     * Estado actual del médico.
     */
    private String status;

    /**
     * Nombre de la ciudad asociada al médico.
     */
    private String nombreCiudad;

}