package com.medicore.api.services;

import com.medicore.api.entities.Medico;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz de servicio encargada de definir
 * las operaciones relacionadas con los médicos.
 */
public interface IMedicoService {

    /**
     * Obtiene la lista de todos los médicos registrados.
     *
     * @return lista de médicos
     */
    List<Medico> findAll();

    /**
     * Busca un médico por su documento.
     *
     * @param documento documento del médico
     * @return médico encontrado o vacío si no existe
     */
    Optional<Medico> findById(String documento);

    /**
     * Guarda un nuevo médico en el sistema.
     *
     * @param medico entidad médico a registrar
     * @return médico guardado
     */
    Medico save(Medico medico);

    /**
     * Actualiza la información de un médico existente.
     *
     * @param documento documento del médico a actualizar
     * @param medico nuevos datos del médico
     * @return médico actualizado o vacío si no existe
     */
    Optional<Medico> update(String documento, Medico medico);

    /**
     * Inhabilita un médico del sistema.
     *
     * @param documento documento del médico
     * @return true si la operación fue exitosa,
     * false en caso contrario
     */
    boolean delete(String documento);
}