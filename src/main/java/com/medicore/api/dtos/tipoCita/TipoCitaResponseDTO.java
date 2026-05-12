package com.medicore.api.dtos.tipoCita;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * DTO utilizado para enviar la información
 * de un TipoCita como respuesta al cliente.
 *
 * <p>Contiene los datos públicos y relevantes
 * de un TipoCita registrados en el sistema.</p>
 *
 * @author Manuel
 */
@Data
@NoArgsConstructor
public class TipoCitaResponseDTO {
    /**
     * id de identificacion de un TipoCita.
     */
    private Integer id;

    /**
     * Nombre del TipoCita.
     */
    private String nombre;
}
