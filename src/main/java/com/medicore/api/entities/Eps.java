package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "eps")
@Data
@AllArgsConstructor @NoArgsConstructor

public class Eps {

    @Id
    @Column(name="codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @OneToMany(mappedBy = "eps")
    private List<Usuario> usuarios;
}
