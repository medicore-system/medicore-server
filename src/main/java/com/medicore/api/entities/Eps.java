package com.medicore.api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Entidad que representa una EPS dentro del sistema.
 *
 * <p>Una EPS (Entidad Promotora de Salud)
 * puede estar asociada a múltiples usuarios.</p>
 *
 * @author Manuel
 */
@Entity
@Table(name = "eps")
@Data
@AllArgsConstructor @NoArgsConstructor

public class Eps {

    /**
     * Código único identificador de la EPS.
     */
    @Id
    @Column(name="codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    /**
     * Nombre de la EPS.
     */
    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    /**
     * Lista de usuarios afiliados a la EPS.
     *
     * <p>Representa la relación uno a muchos
     * entre una EPS y sus usuarios.</p>
     *
     * <p>Una EPS puede tener múltiples usuarios
     * registrados.</p>
     */
    @OneToMany(mappedBy = "eps")
    private List<Usuario> usuarios;
}
