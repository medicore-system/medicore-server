package com.medicore.api.repositories.hospital;

import com.medicore.api.entities.hospital.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad {@link Hospital}.
 *
 * <p>Esta interfaz proporciona operaciones CRUD y consultas
 * personalizadas para la entidad {@code Hospital}, heredando
 * los métodos definidos en {@link JpaRepository}.</p>
 *
 * <p>Al extender {@code JpaRepository}, se obtienen automáticamente
 * métodos como guardar, buscar por ID, buscar todos, eliminar,
 * entre otros, sin necesidad de implementación explícita.</p>
 *
 * <p>El tipo de la entidad es {@link Hospital} y el tipo de su
 * identificador es {@link String}.</p>
 *
 * @author Juan Sebastián López Guzmán
 * @see Hospital
 * @see JpaRepository
 */
@Repository
public interface HospitalRepository extends JpaRepository<Hospital, String> {

    /**
     * Cuenta la cantidad de hospitales registrados en una ciudad específica
     * a partir del código de la ciudad.
     *
     * <p>Spring Data JPA genera automáticamente la implementación de
     * este método basándose en su nombre, siguiendo la convención de
     * consultas derivadas.</p>
     *
     * @param codigoCiudad código único de la ciudad de la cual se desea
     *                     contar los hospitales registrados
     * @return el número total de hospitales asociados a la ciudad
     *         especificada. Si no hay hospitales registrados en dicha
     *         ciudad, retorna {@code 0}
     */
    Long countByCiudadCodigo(String codigoCiudad);

    /**
     * Lista todos los hospitales que pertenezcan a una ciudad especifica
     * que esten activos
     *
     * @param codigoCiudad codigo unico de la ciudad
     */
    List<Hospital> findByCiudadCodigoAndEstadoTrue(String codigoCiudad);
}