package com.medicore.api.entities.areainterna;

import com.medicore.api.entities.hospital.Hospital;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa la relación entre un hospital y un área interna
 * en el sistema MediCore.
 *
 * <p>Esta clase mapea la tabla {@code hospital_area_interna} de la base de datos
 * y actúa como una entidad asociativa que vincula un hospital con un área interna
 * específica, permitiendo agregar información adicional como nombre y descripción
 * propios de esa relación.</p>
 *
 * <p>Utiliza Lombok para generar automáticamente los métodos
 * getter, setter y el constructor sin argumentos requerido por JPA.</p>
 *
 * @author Juan Sebastián López Guzmán
 * @see Hospital
 * @see AreaInterna
 */
@Entity
@Table(name = "hospital_area_interna")
@Getter
@Setter
@NoArgsConstructor
public class HospitalAreaInterna {

    /**
     * Código único que identifica la relación hospital-área interna.
     *
     * <p>Actúa como clave primaria de la entidad. Su valor es asignado
     * manualmente y tiene una longitud máxima de 50 caracteres.</p>
     */
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    /**
     * Nombre descriptivo del área interna dentro del hospital.
     *
     * <p>Este campo es opcional y tiene una longitud máxima de
     * 100 caracteres.</p>
     */
    @Column(name = "nombre", length = 100)
    private String nombre;

    /**
     * Descripción detallada del área interna dentro del hospital.
     *
     * <p>Este campo es opcional y tiene una longitud máxima de
     * 250 caracteres.</p>
     */
    @Column(name = "descripcion", length = 250)
    private String descripcion;

    /**
     * Hospital al que pertenece esta área interna.
     *
     * <p>Relación muchos a uno con la entidad {@link Hospital}.
     * La carga se realiza de forma perezosa (LAZY) por eficiencia.
     * Este campo es obligatorio.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_hospital", nullable = false)
    private Hospital hospital;

    /**
     * Área interna genérica asociada a esta relación.
     *
     * <p>Relación muchos a uno con la entidad {@link AreaInterna}.
     * La carga se realiza de forma perezosa (LAZY) por eficiencia.
     * Este campo es obligatorio.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_area_interna", nullable = false)
    private AreaInterna areaInterna;
}