package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "prefijo", nullable = false, unique = true, length = 10)
    private String prefijo;
}