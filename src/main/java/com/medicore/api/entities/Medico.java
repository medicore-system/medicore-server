package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table (name = "medico")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "especialidad")
public class Medico {
    @Id
    @Column(name = "documento")
    private String documento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name =  "telefono")
    private String telefono;

    @Column(name = "correo")
    private String email;

    @Column(name = "estado", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado;

    @OneToOne
    @JoinColumn(name = "id_especialidad")
    private Especialidad especialidad;


    @OneToOne
    @JoinColumn(name = "codigo_ciudad")
    private Ciudad ciudad;
}
