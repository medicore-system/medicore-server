package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
/**
 * Entidad que representa un usuario dentro del sistema.
 *
 * <p>Un usuario puede desempeñar diferentes roles,
 * como PACIENTE o ADMIN, y estar asociado a una EPS,
 * una ciudad y múltiples citas médicas.</p>
 *
 * @author Manuel
 */
@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Usuario {

    /**
     * Documento único de identificación del usuario.
     */
    @Id
    @Column(name = "documento", nullable = false, length = 100)
    private String documento;

    /**
     * Nombre del usuario.
     */
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Apellido del usuario.
     */
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    /**
     * Correo electrónico del usuario.
     */
    @Column(name = "correo", nullable = false, length = 100)
    private String correo;

    /**
     * Número telefónico del usuario.
     */
    @Column(name = "telefono", nullable = false, length = 100)
    private String telefono;

    /**
     * Fecha de nacimiento del usuario.
     *
     * <p>Este campo no puede ser insertado ni actualizado
     * directamente desde la entidad.</p>
     */
    @Column(name = "fecha_nacimiento", insertable = false, updatable = false)
    private LocalDate fechaNacimiento;

/* Se quitó lo siguiente:
    @Column(name = "contrasena",  nullable = false, length = 50)
    private String contraseña;

    Y se cambió por:*/
    /**
     * Contraseña del usuario para autenticación.
     */
    @Column(name = "contrasena", nullable = false, length = 200) // Se agregó el nullable = false.
    private String contrasena;


    /**
     * EPS asociada al usuario.
     *
     * <p>Muchos usuarios pueden pertenecer
     * a una misma EPS.</p>
     */
    @ManyToOne
    @JoinColumn(name = "codigo_eps", nullable = false)
    private Eps eps;

    /**
     * Ciudad asociada al usuario.
     *
     * <p>Muchos usuarios pueden pertenecer
     * a una misma ciudad.</p>
     */
    @ManyToOne
    @JoinColumn(name = "codigo_ciudad", nullable = false)
    private Ciudad ciudad;

    /**
     * Estado actual del usuario.
     *
     * <p>
     * true  = usuario habilitado<br>
     * false = usuario inhabilitado
     * </p>
     *
     * <p>Por defecto el estado es true.</p>
     */
    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado = true;

    /**
     * Rol asignado al usuario dentro del sistema.
     *
     * <p>Valores posibles:
     * <ul>
     *     <li>PACIENTE</li>
     *     <li>ADMIN</li>
     * </ul>
     * </p>
     *
     * <p>Por defecto el rol es "PACIENTE".</p>
     */
    @Column(name = "rol",  nullable = false)
    private String rol = "PACIENTE";

    /**
     * Lista de citas asociadas al usuario.
     *
     * <p>Representa la relación uno a muchos entre
     * un usuario y sus citas médicas.</p>
     */
    @OneToMany(mappedBy = "usuario")
    private List<Cita> citas;
}
