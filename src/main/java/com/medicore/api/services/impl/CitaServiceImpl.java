package com.medicore.api.services.impl;

import com.medicore.api.entities.Cita;
import com.medicore.api.repositories.ICitaRepository;
import com.medicore.api.services.ICitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Implementación del servicio encargado de la lógica
 * de negocio relacionada con las citas médicas.
 *
 * <p>Esta clase administra las operaciones CRUD
 * y el cambio de estado de las citas.</p>
 *
 * @author Manuel
 */
@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements ICitaService {

    /**
     * Repositorio encargado de la persistencia
     * de las citas médicas.
     */
    private final ICitaRepository  citaRepository;

    /**
     * Guarda una cita en la base de datos.
     *
     * @param cita entidad cita a guardar.
     * @return cita almacenada.
     */
    @Override
    public Cita save(Cita cita) {
        return citaRepository.save(cita);
    }

    /**
     * Obtiene todas las citas registradas.
     *
     * @return lista de citas.
     */
    @Override
    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    /**
     * Busca una cita por su código.
     *
     * @param codigo código único de la cita.
     * @return Optional con la cita encontrada
     * o vacío si no existe.
     */
    @Override
    public Optional<Cita> findByCodigo(String codigo) {
        return citaRepository.findById(codigo);
    }

    /**
     * Busca una cita utilizando el documento
     * del paciente asociado.
     *
     * @param documento_paciente documento del paciente.
     * @return Optional con la cita encontrada
     * o vacío si no existe.
     */
    @Override
    public Optional<Cita>findByDocumento(String documento_paciente) {
        return citaRepository.findByUsuarioDocumento(documento_paciente);
    }

    /**
     * Aprueba una cita médica.
     *
     * <p>El estado de la cita cambia a "APROBADA".</p>
     *
     * @param codigo código de la cita.
     * @return Optional con la cita actualizada
     * o vacío si no existe.
     */
    @Override
    public Optional<Cita> aprobar(String codigo) {
        return citaRepository.findById(codigo).map(existing ->{
            existing.setEstado("APROBADA");
            return citaRepository.save(existing);
        });
    }

    /**
     * Deniega una cita médica.
     *
     * <p>El estado de la cita cambia a "DENEGADA".</p>
     *
     * @param codigo código de la cita.
     * @return Optional con la cita actualizada
     * o vacío si no existe.
     */
    @Override
    public Optional<Cita> denegar(String codigo) {
        return citaRepository.findById(codigo).map(existing ->{
            existing.setEstado("DENEGADA");
            return citaRepository.save(existing);
        });
    }
}
