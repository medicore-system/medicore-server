package com.medicore.api.repositories.areinterna;

import com.medicore.api.entities.areainterna.HospitalAreaInterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio Spring Data JPA para la entidad {@link HospitalAreaInterna}.
 *
 * <p>Esta interfaz proporciona operaciones CRUD y consultas
 * personalizadas para la entidad {@code HospitalAreaInterna}, heredando
 * los métodos definidos en {@link JpaRepository}.</p>
 *
 * <p>Al extender {@code JpaRepository}, se obtienen automáticamente
 * métodos como guardar, buscar por ID, buscar todos, eliminar,
 * entre otros, sin necesidad de implementación explícita.</p>
 *
 * <p>El tipo de la entidad es {@link HospitalAreaInterna} y el tipo
 * de su identificador es {@link String}.</p>
 *
 * @author Cristian Camilo Salazar Arenas
 * @see HospitalAreaInterna
 * @see JpaRepository
 */
@Repository
public interface HospitalAreaInternaRepository extends JpaRepository<HospitalAreaInterna, String> {

    /**
     * Busca todas las áreas internas asociadas a un hospital específico
     * a partir del código del hospital.
     *
     * <p>Spring Data JPA genera automáticamente la implementación de
     * este método basándose en su nombre, siguiendo la convención de
     * consultas derivadas.</p>
     *
     * @param codigoHospital código único del hospital del cual se desean
     *                       obtener las áreas internas asociadas
     * @return una lista de objetos {@link HospitalAreaInterna} que pertenecen
     *         al hospital especificado. Si el hospital no tiene áreas internas
     *         asociadas, la lista estará vacía
     */
    List<HospitalAreaInterna> findByHospitalCodigo(String codigoHospital);
}