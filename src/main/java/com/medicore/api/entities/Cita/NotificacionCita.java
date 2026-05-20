package com.medicore.api.entities.Cita;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificacion_cita")
@Data
@AllArgsConstructor@NoArgsConstructor
public class NotificacionCita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(name = "estado", nullable = false)
    private Boolean estado = false;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "correo_destino")
    private String correo;

    @OneToOne
    @JoinColumn(name = "codigo_cita")
    private Cita cita;
}
