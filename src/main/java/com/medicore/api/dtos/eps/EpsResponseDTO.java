package com.medicore.api.dtos.eps;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO utilizado para enviar la información
 * de una eps como respuesta al cliente.
 *
 * <p>Contiene los datos públicos y relevantes
 * de una Eps registrada en el sistema.</p>
 *
 * @author Manuel
 */
@Data
@NoArgsConstructor
public class EpsResponseDTO {
    /**
     * Codigo de identificacion de una eps.
     */
    private String codigo;

    /**
     * Nombre de la eps.
     */
    private String nombre;
}
