package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ciudad")
@Getter
@Setter
@NoArgsConstructor
public class Ciudad {

    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    //Esto lo tienen que modificar los que hagan la seccion de departamento
    @Column(name = "id_departamento", nullable = false)
    private Integer idDepartamento;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}