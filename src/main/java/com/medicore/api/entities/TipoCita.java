package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_cita")
@Data
@AllArgsConstructor @NoArgsConstructor
public class TipoCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @OneToOne(mappedBy = "tipoCita")
    private Cita cita;
}
