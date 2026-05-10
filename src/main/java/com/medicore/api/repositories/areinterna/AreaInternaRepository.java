package com.medicore.api.repositories.areinterna;

import com.medicore.api.entities.areainterna.AreaInterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la entidad {@link AreaInterna}.
 *
 * <p>Esta interfaz proporciona operaciones CRUD y consultas
 * personalizadas para la entidad {@code AreaInterna}, heredando
 * los métodos definidos en {@link JpaRepository}.</p>
 *
 * <p>Al extender {@code JpaRepository}, se obtienen automáticamente
 * métodos como guardar, buscar por ID, buscar todos, eliminar,
 * entre otros, sin necesidad de implementación explícita.</p>
 *
 * <p>El tipo de la entidad es {@link AreaInterna} y el tipo de su
 * identificador es {@link String}.</p>
 *
 * @author Cristian Camilo Salazar Arenas
 * @see AreaInterna
 * @see JpaRepository
 */
@Repository
public interface AreaInternaRepository extends JpaRepository<AreaInterna, String> {
}