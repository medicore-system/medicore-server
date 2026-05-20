package com.medicore.api.repositories.cita;

import com.medicore.api.entities.Cita.Cita;
import com.medicore.api.repositories.reportes.ReporteCitaRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
public interface ICitaRepository extends JpaRepository<Cita, String>, ReporteCitaRepositoryCustom {

    /**
     * Busca una cita asociada al documento de un usuario/paciente.
     *
     * @param documento_paciente documento del paciente.
     * @return un Optional con la cita encontrada,
     * o vacío si no existe.
     */
    List<Cita> findByUsuarioDocumento(String documento_paciente);

    /**
     * Busca una cita asociada al documento de un medico.
     *
     * @param documento_medico documento del medico.
     * @return Lista con las citas encontradas,
     * o vacío si no existe.
     */
    List<Cita> findByMedicoDocumento(String documento_medico);

    /**
     * Verifica si ya existe una cita para el médico en esa fecha/hora exacta.
     *
     * <p>Usado antes de crear una cita para evitar doble booking.
     * Se debe llamar desde el servicio o controller antes de hacer el save.</p>
     *
     * @param documentoMedico documento del médico.
     * @param fecha           fecha y hora exacta del slot.
     * @return true si el slot ya está ocupado.
     */
    boolean existsByMedicoDocumentoAndFecha(String documentoMedico, LocalDateTime fecha);

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
