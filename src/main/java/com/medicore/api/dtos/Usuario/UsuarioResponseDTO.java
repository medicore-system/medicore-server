package com.medicore.api.dtos.Usuario;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO utilizado para enviar la información
 * de un usuario como respuesta al cliente.
 *
 * <p>Contiene los datos públicos y relevantes
 * de un usuario registrados en el sistema.</p>
 *
 * @author Manuel
 */
@Data
@NoArgsConstructor
public class UsuarioResponseDTO {
    /**
     * Documento de identificación del usuario.
     */
    private String documento;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     */
    private String apellido;

    /**
     * Correo electrónico del usuario.
     */
    private String correo;

    /**
     * Nombre de la EPS asociada al usuario.
     */
    private String eps;

    /**
     * Nombre de la ciudad asociada al usuario.
     */
    private String ciudad;

    /**
     * Número telefónico del usuario.
     */
    private String telefono;
    /**
     * Rol asignado al usuario.
     *
     * <p>Valores posibles:
     * <ul>
     *     <li>PACIENTE</li>
     *     <li>ADMIN</li>
     * </ul>
     * </p>
     */
    private String rol;

    /**
     * Estado actual del usuario.
     *
     * <p>
     * true  = usuario habilitado<br>
     * false = usuario inhabilitado
     * </p>
     */
    private boolean estado;

}
