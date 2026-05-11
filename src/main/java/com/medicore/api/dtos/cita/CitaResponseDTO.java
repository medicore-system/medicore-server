package com.medicore.api.dtos.cita;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * DTO utilizado para enviar la información
 * de una cita médica como respuesta al cliente.
 *
 * <p>Contiene los datos relevantes de una cita,
 * incluyendo información del paciente, médico,
 * hospital y tipo de cita.</p>
 *
 * @author Manuel
 */
@Data
@NoArgsConstructor
public class CitaResponseDTO {
    /**
     * Código único de la cita.
     */

    private String codigo;
    /**
     * Estado actual de la cita.
     *
     * <p>Valores posibles:
     * <ul>
     *     <li>APROBADA</li>
     *     <li>PENDIENTE</li>
     *     <li>DENEGADA</li>
     * </ul>
     * </p>
     */
    private String estado;

    /**
     * Fecha y hora programada de la cita.
     */
    private LocalDateTime fecha;

    /**
     * Hora específica asignada para la cita.
     */
    private String hora;

    /**
     * Valor monetario de la cita.
     */
    private BigDecimal costo;

    /**
     * Nombre del tipo de cita.
     */
    private String tipoCita;

    /**
     * Nombre del usuario/paciente asociado a la cita.
     */
    private String nombreUsuario;

    /**
     * Documento o identificador del médico encargado.
     */
    private String medico;

    /**
     * Nombre del hospital donde se realizará la cita.
     */
    private String Hospital;

}
