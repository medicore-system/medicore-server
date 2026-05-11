package com.medicore.api.repositories;

import com.medicore.api.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * Repositorio encargado de la persistencia y consulta
 * de entidades {@link Cita}.
 *
 * <p>Extiende {@link JpaRepository} para proporcionar
 * operaciones CRUD y consultas personalizadas.</p>
 *
 * @author Manuel
 */
@Repository
public interface ICitaRepository extends JpaRepository<Cita, String> {

    /**
     * Busca una cita asociada al documento de un usuario/paciente.
     *
     * @param documento_paciente documento del paciente.
     * @return un Optional con la cita encontrada,
     * o vacío si no existe.
     */
    Optional<Cita> findByUsuarioDocumento(String documento_paciente);

    /**
     * Obtiene el valor numérico máximo utilizado
     * en los códigos de citas registrados.
     *
     * <p>La consulta extrae la parte numérica del código
     * de cita y retorna el valor más alto encontrado.</p>
     *
     * <p>Ejemplo:
     * <ul>
     *     <li>CIT001</li>
     *     <li>CIT002</li>
     *     <li>CIT003</li>
     * </ul>
     * retornará: 3
     * </p>
     *
     * @return número máximo de secuencia utilizado
     * en los códigos de citas.
     */
    @Query(value = "SELECT COALESCE(MAX(CAST(SUBSTRING(codigo FROM 4) AS INTEGER)), 0) FROM cita", nativeQuery = true)
    int findMaxCodigoSquence();
}
