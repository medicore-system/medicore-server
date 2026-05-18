package com.medicore.api.repositories;

import com.medicore.api.entities.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad {@link Servicio}.
 *
 * <p>
 * Esta interfaz centraliza el acceso a datos de los servicios médicos.
 * Usa métodos derivados de Spring Data JPA para evitar consultas quemadas
 * dentro del código fuente.
 * </p>
 *
 * <p>
 * La intención de este repositorio es mantener una separación clara entre
 * la lógica de negocio y la persistencia, respetando el principio de
 * responsabilidad única.
 * </p>
 */
@Repository
public interface IServicioRepository extends JpaRepository<Servicio, String> {

    /**
     * Obtiene todos los servicios ordenados ascendentemente por código.
     *
     * @return lista de servicios ordenados por código.
     */
    List<Servicio> findAllByOrderByCodigoAsc();

    /**
     * Verifica si existe un servicio con el nombre indicado, ignorando mayúsculas y minúsculas.
     *
     * @param nombre nombre del servicio.
     * @return {@code true} si existe un servicio con ese nombre; de lo contrario, {@code false}.
     */
    boolean existsByNombreIgnoreCase(String nombre);

    /**
     * Verifica si existe otro servicio con el mismo nombre, excluyendo el código indicado.
     *
     * <p>
     * Este método se utiliza principalmente durante la edición de servicios,
     * para permitir que un servicio conserve su propio nombre sin ser detectado
     * como duplicado.
     * </p>
     *
     * @param nombre nombre del servicio.
     * @param codigo código del servicio que se debe excluir de la validación.
     * @return {@code true} si otro servicio ya usa el nombre; de lo contrario, {@code false}.
     */
    boolean existsByNombreIgnoreCaseAndCodigoNot(String nombre, String codigo);

    /**
     * Busca servicios por coincidencia parcial en código, nombre o descripción.
     *
     * @param codigo término aplicado sobre el código.
     * @param nombre término aplicado sobre el nombre.
     * @param descripcion término aplicado sobre la descripción.
     * @return lista de servicios que coinciden con alguno de los campos evaluados.
     */
    List<Servicio> findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrderByCodigoAsc(
            String codigo,
            String nombre,
            String descripcion
    );

    /**
     * Busca servicios cuyo código inicie con el prefijo indicado.
     *
     * <p>
     * Este método se utiliza para calcular el siguiente consecutivo de un tipo de servicio,
     * por ejemplo: MED-001, MED-002, EXA-001.
     * </p>
     *
     * @param prefijoConGuion prefijo del código incluyendo el guion final, por ejemplo {@code MED-}.
     * @return lista de servicios cuyo código inicia con el prefijo indicado.
     */
    List<Servicio> findByCodigoStartingWith(String prefijoConGuion);
}