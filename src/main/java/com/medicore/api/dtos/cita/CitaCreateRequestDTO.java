package com.medicore.api.dtos.cita;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * DTO utilizado para recibir la información necesaria
 * para la creación de una cita médica.
 *
 * <p>Contiene los datos enviados por el cliente
 * desde la petición HTTP.</p>
 *
 * @author Manuel
 */
@Data
@NoArgsConstructor
public class CitaCreateRequestDTO {
    /**
     * Fecha y hora de la cita médica.
     */
    private LocalDateTime fecha;

    /**
     * Costo asociado a la cita médica.
     */
    private BigDecimal costo;
    /**
     * identificador del tipo asociado a la cita médica.
     */
    private Integer id_tipo;
    /**
     * documento del paciente asociado a la cita médica.
     */
    private String documento_paciente;
    /**
     * documento del medico asociado a la cita médica.
     */
    private String documento_medico;
    /**
     * codigo del hospital asociado a la cita médica.
     */
    private String codigo_hospital;
}
