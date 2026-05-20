package com.medicore.api.entities.Cita;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una notificación enviada al paciente sobre una cita médica.
 * Registra el correo de destino, la descripción y el estado de lectura de la notificación.
 */
@Entity
@Table(name = "notificacion_cita")
@Data
@AllArgsConstructor@NoArgsConstructor
public class NotificacionCita {

    /** Identificador autogenerado de la notificación. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    /** Indica si la notificación ya fue leída o procesada por el paciente. */
    @Column(name = "estado", nullable = false)
    private Boolean estado = false;

    /** Mensaje o descripción del contenido de la notificación. */
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    /** Dirección de correo electrónico del destinatario. */
    @Column(name = "correo_destino")
    private String correo;

    /** Cita médica a la que hace referencia esta notificación. */
    @OneToOne
    @JoinColumn(name = "codigo_cita")
    private Cita cita;
}
