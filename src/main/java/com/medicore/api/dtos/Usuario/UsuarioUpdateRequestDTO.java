package com.medicore.api.dtos.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO utilizado para recibir la información
 * necesaria para actualizar un usuario existente.
 *
 * <p>Permite modificar parcialmente los datos
 * principales asociados al usuario.</p>
 *
 * @author Manuel
 */
@Data
@NoArgsConstructor
public class UsuarioUpdateRequestDTO {

    /**
     * Nuevo nombre del usuario.
     */
    private String nombre;

    /**
     * Nuevo apellido del usuario.
     */
    private String apellido;

    /**
     * Nuevo número telefónico del usuario.
     */
    private String telefono;

    /**
     * Código de la nueva EPS asociada al usuario.
     */
    private String codigo_eps;

    /**
     * Código de la nueva ciudad asociada al usuario.
     */
    private String codigo_ciudad;
}
