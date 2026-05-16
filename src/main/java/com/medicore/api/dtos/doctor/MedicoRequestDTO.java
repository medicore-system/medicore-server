package com.medicore.api.dtos.doctor;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO utilizado para recibir la información
 * enviada desde el frontend al registrar
 * un médico en el sistema.
 *
 * <p>
 * Contiene los datos necesarios para crear
 * un nuevo médico.
 * </p>
 */
@Data
@NoArgsConstructor
//Request --> Lo que el frontend envia(la info que enviamos)
public class MedicoRequestDTO {

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
     * Identificador de la especialidad médica.
     */
    private Integer idEspecialidad;

    /**
     * Número telefónico del médico.
     */
    private String telefono;

    /**
     * Correo electrónico del médico.
     */
    private String email;

    /**
     * Código de la ciudad asociada al médico.
     */
    private String codigoCiudad;

    /**
     * Estado del médico dentro del sistema.
     */
    private Boolean estado;

}