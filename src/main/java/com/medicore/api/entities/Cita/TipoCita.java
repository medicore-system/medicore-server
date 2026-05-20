package com.medicore.api.entities.Cita;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "tipoCita")
    private List<Cita> cita;
}
