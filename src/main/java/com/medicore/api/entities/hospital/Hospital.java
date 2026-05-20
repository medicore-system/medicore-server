package com.medicore.api.entities.hospital;

import com.medicore.api.entities.AsignacionMedico;
import com.medicore.api.entities.Cita.Cita;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.areainterna.HospitalAreaInterna;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Entidad JPA que representa un hospital en el sistema MediCore.
 *
 * <p>Esta clase mapea la tabla {@code hospital} de la base de datos
 * y contiene la información de identificación, contacto, ubicación
 * y estado de cada hospital registrado en el sistema, así como la
 * relación con sus áreas internas.</p>
 *
 * <p>Utiliza Lombok para generar automáticamente los métodos
 * getter, setter, constructores y el patrón de diseño builder.
 * El estado del hospital se inicializa por defecto como activo ({@code true}).</p>
 *
 * @author Juan Sebastián López Guzmán
 * @see Ciudad
 * @see HospitalAreaInterna
 */
@Entity
@Table(name = "hospital")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hospital {

    /**
     * Código único que identifica el hospital.
     *
     * <p>Actúa como clave primaria de la entidad. Su valor es asignado
     * manualmente y tiene una longitud máxima de 50 caracteres.</p>
     */
    @Id
    @Column(name = "codigo", length = 50)
    private String codigo;

    /**
     * Nombre del hospital.
     *
     * <p>Este campo es obligatorio y tiene una longitud máxima de
     * 50 caracteres.</p>
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Dirección física del hospital.
     *
     * <p>Este campo es obligatorio y tiene una longitud máxima de
     * 150 caracteres.</p>
     */
    @Column(name = "direccion", nullable = false, length = 150)
    private String direccion;

    /**
     * Número de teléfono de contacto del hospital.
     *
     * <p>Este campo es obligatorio y tiene una longitud máxima de
     * 20 caracteres.</p>
     */
    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    /**
     * Estado del hospital.
     *
     * <p>Indica si el hospital se encuentra activo ({@code true})
     * o inactivo ({@code false}) en el sistema.
     * Este campo es obligatorio y se inicializa por defecto en {@code true}
     * al utilizar el patrón builder.</p>
     */
    @Column(name = "estado", nullable = false)
    @Builder.Default
    private Boolean estado = true;

    /**
     * Ciudad donde se ubica el hospital.
     *
     * <p>Relación muchos a uno con la entidad {@link Ciudad}.
     * La carga se realiza de forma perezosa (LAZY) por eficiencia.
     * Este campo es obligatorio.</p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_ciudad", nullable = false)
    private Ciudad ciudad;

    /**
     * Lista de áreas internas asociadas al hospital.
     *
     * <p>Relación uno a muchos con la entidad {@link HospitalAreaInterna}.
     * La carga se realiza de forma perezosa (LAZY) por eficiencia.
     * Se inicializa como una lista vacía por defecto al utilizar el patrón builder.</p>
     */
    @OneToMany(mappedBy = "hospital", fetch = FetchType.LAZY)
    @Builder.Default
    private List<HospitalAreaInterna> areasInternas = new ArrayList<>();

    /**
     * Lista de citas asociadas al hospital.
     *
     * <p>Representa la relación uno a muchos entre
     * un hospital y sus citas médicas.</p>
     *
     * <p>Un hospital puede tener múltiples citas
     * registradas.</p>
     */
    @OneToMany(mappedBy = "hospital")
    private List<Cita> citas;

    /**
     * Lista de asignaciones asociadas al hospital.
     *
     * <p>Representa la relación uno a muchos entre
     * un hospital y sus asignaciones médicas.</p>
     *
     * <p>Un hospital puede tener múltiples asignaciones
     * registradas.</p>
     */
    @OneToMany(mappedBy = "hospital")
    private List<AsignacionMedico> asiganciones;
}