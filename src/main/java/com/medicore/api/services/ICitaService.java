package com.medicore.api.services;

import com.medicore.api.entities.Cita.Cita;

import java.util.List;
import java.util.Optional;
/**
 * Interfaz que define las operaciones de negocio
 * relacionadas con las citas médicas.
 *
 * <p>Contiene los métodos necesarios para:
 * <ul>
 *     <li>Registrar citas</li>
 *     <li>Consultar citas</li>
 *     <li>Aprobar citas</li>
 *     <li>Denegar citas</li>
 * </ul>
 * </p>
 *
 * @author Manuel
 */
public interface ICitaService {

    /**
     * Guarda una cita en el sistema.
     *
     * @param cita entidad cita a guardar.
     * @return cita almacenada.
     */
    Cita save(Cita cita);

    /**
     * Obtiene todas las citas registradas.
     *
     * @return lista de citas.
     */
    List<Cita> findAll();

    /**
     * Busca una cita por su código.
     *
     * @param codigo código único de la cita.
     * @return Optional con la cita encontrada
     * o vacío si no existe.
     */
    Optional<Cita> findByCodigo(String codigo);

    /**
     * Busca una cita asociada al documento
     * de un paciente.
     *
     * @param documento_paciente documento del paciente.
     * @return Lista de citas encontradas
     * o vacío si no existe.
     */
    List<Cita> findByDocumento(String documento_paciente);

    /**
     * Busca una cita asociada al documento
     * de un medico.
     *
     * @param documento_medico documento del medico.
     * @return Listade citas encontradas
     * o vacío si no existe.
     */
    List<Cita> findByMedico(String documento_medico);

    /**
     * Aprueba una cita médica.
     *
     * <p>El estado de la cita cambia a "APROBADA".</p>
     *
     * @param codigo código de la cita.
     * @return Optional con la cita actualizada
     * o vacío si no existe.
     */
    Optional<Cita> aprobar(String codigo);

    /**
     * Deniega una cita médica.
     *
     * <p>El estado de la cita cambia a "DENEGADA".</p>
     *
     * @param codigo código de la cita.
     * @return Optional con la cita actualizada
     * o vacío si no existe.
     */
    Optional<Cita> denegar(String codigo);

}
