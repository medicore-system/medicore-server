package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor @NoArgsConstructor

public class Usuario {

    @Id
    @Column(name = "documento", nullable = false, length = 50)
    private String documento;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 50)
    private String apellido;

    @Column(name = "correo", nullable = false, length = 50)
    private String correo;

    @Column(name = "telefono", nullable = false, length = 50)
    private String telefono;

    @Column(name = "fecha_nacimiento", insertable = false, updatable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "contrasena", length = 50)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "codigo_eps", nullable = false)
    private Eps eps;

    @ManyToOne
    @JoinColumn(name = "codigo_ciudad", nullable = false)
    private Ciudad ciudad;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado = true;

    @Column(name = "rol",  nullable = false)
    private String rol = "PACIENTE";
}
