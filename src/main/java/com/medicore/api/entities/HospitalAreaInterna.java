package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hospital_area_interna")
@Getter
@Setter
@NoArgsConstructor
public class HospitalAreaInterna {

    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 250)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_hospital", nullable = false)
    private Hospital hospital;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_area_interna", nullable = false)
    private AreaInterna areaInterna;
}