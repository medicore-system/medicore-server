package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "area_interna")
@Getter
@Setter
@NoArgsConstructor
public class AreaInterna {

    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;
}