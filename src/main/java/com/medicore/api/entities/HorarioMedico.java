package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "horario_medico")
@Data
@AllArgsConstructor @NoArgsConstructor
public class HorarioMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(name = "horario", nullable = false)
    private String horario;

    @Column(name = "siguiente_horario",  nullable = false)
    private String siguienteHorario;

    @OneToMany(mappedBy = "horario")
    private List<AsignacionMedico> asignacionMedicos;
}
