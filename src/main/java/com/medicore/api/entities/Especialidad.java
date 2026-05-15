package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "especialidad")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "medico")
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @OneToOne(mappedBy = "especialidad")
    private Medico medico;

    @OneToMany(mappedBy = "especialidad")
    private List<Cita> citas;
}
