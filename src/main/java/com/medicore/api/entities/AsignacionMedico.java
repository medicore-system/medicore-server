package com.medicore.api.entities;

import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "asignacion_medico")
@Data
@NoArgsConstructor @AllArgsConstructor
public class AsignacionMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @ManyToOne
    @JoinColumn(name = "documento_medico", nullable = false)
    private Medico medico;

    @ManyToOne
    @JoinColumn(name = "codigo_hospital", nullable = false)
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "codigo_ciudad",  nullable = false)
    private Ciudad ciudad;

    @ManyToOne
    @JoinColumn(name = "codigo_horario", nullable = false)
    private HorarioMedico horario;

    @Column(name = "estado",  nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado = true;
}
