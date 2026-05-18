package com.medicore.api.services.impl;

import com.medicore.api.dtos.hospital.HospitalRequest;
import com.medicore.api.dtos.hospital.HospitalResponse;
import com.medicore.api.dtos.hospital.HospitalUpdateRequest;
import com.medicore.api.entities.Ciudad;
import com.medicore.api.entities.hospital.Hospital;
import com.medicore.api.repositories.ICiudadRepository;
import com.medicore.api.repositories.hospital.HospitalRepository;
import com.medicore.api.services.IHospitalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio {@link IHospitalService} que gestiona
 * la lógica de negocio relacionada con los hospitales en el sistema MediCore.
 *
 * <p>Esta clase proporciona la implementación de los métodos definidos en la
 * interfaz {@code IHospitalService}, incluyendo la creación, actualización
 * y consulta de hospitales.</p>
 *
 * <p>Utiliza los repositorios {@link HospitalRepository} y
 * {@link ICiudadRepository} para acceder a la capa de persistencia.
 * Todas las operaciones de escritura son transaccionales, mientras que
 * las de lectura se marcan como de solo lectura para optimización.</p>
 *
 * <p>Se apoya en Lombok para la inyección de dependencias por constructor
 * a través de la anotación {@link RequiredArgsConstructor}.</p>
 *
 * @author Juan Sebastián López Guzmán
 * @author Cristian Camilo Salazar Arenas
 * @see IHospitalService
 * @see HospitalRepository
 * @see ICiudadRepository
 */
@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements IHospitalService {

    /** Repositorio para operaciones de persistencia de hospitales. */
    private final HospitalRepository hospitalRepository;

    /** Repositorio para operaciones de persistencia de ciudades. */
    private final ICiudadRepository ciudadRepository;

    /**
     * Crea un nuevo hospital en el sistema.
     *
     * <p>Valida la existencia de la ciudad asociada antes de realizar la
     * creación. El estado del hospital se inicializa como activo ({@code true}).
     * La operación es transaccional.</p>
     *
     * @param request objeto con los datos necesarios para crear el hospital,
     *                incluyendo el código de la ciudad donde se ubica
     * @return un objeto {@link HospitalResponse} con la información del
     *         hospital recién creado
     * @throws EntityNotFoundException si no se encuentra una ciudad con el
     *                                 código especificado en el request
     */
    @Override
    @Transactional
    public HospitalResponse createHospital(HospitalRequest request) {
        Ciudad ciudad = validarCiudad(request.getCodigoCiudad());
        Hospital hospital = Hospital.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .estado(true)
                .ciudad(ciudad)
                .build();

        Hospital saved = hospitalRepository.save(hospital);
        return toResponse(saved);
    }

    /**
     * Convierte una entidad {@link Hospital} a su correspondiente
     * DTO de respuesta {@link HospitalResponse}.
     *
     * @param hospital la entidad que se desea convertir a DTO
     * @return un objeto {@link HospitalResponse} con los datos mapeados
     *         desde la entidad, incluyendo el código y nombre de la
     *         ciudad donde se ubica el hospital
     */
    private HospitalResponse toResponse(Hospital hospital) {
        return HospitalResponse.builder()
                .codigo(hospital.getCodigo())
                .nombre(hospital.getNombre())
                .direccion(hospital.getDireccion())
                .telefono(hospital.getTelefono())
                .estado(hospital.getEstado())
                .codigoCiudad(hospital.getCiudad().getCodigo())
                .nombreCiudad(hospital.getCiudad().getNombre())
                .build();
    }

    /**
     * Actualiza la información de un hospital existente identificado por su código.
     *
     * <p>Valida la existencia del hospital y de la ciudad asociada antes de
     * realizar la actualización. Construye un nuevo objeto {@link Hospital}
     * con los datos actualizados manteniendo el código original.
     * La operación es transaccional.</p>
     *
     * @param request objeto con los datos actualizados del hospital,
     *                incluyendo el nuevo código de ciudad
     * @param codigo  código único del hospital que se desea actualizar
     * @return un objeto {@link HospitalResponse} con la información
     *         actualizada del hospital
     * @throws EntityNotFoundException si no se encuentra un hospital con el
     *                                 código especificado o si no se encuentra
     *                                 una ciudad con el código proporcionado
     *                                 en el request
     */
    @Override
    @Transactional
    public HospitalResponse updateHospital(HospitalUpdateRequest request, String codigo) {
        Hospital hospital = validarHospital(codigo);
        Ciudad ciudad = validarCiudad(request.getCodigoCiudad());
        Hospital hospitalBuilder = Hospital.builder()
                .codigo(hospital.getCodigo())
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .telefono(request.getTelefono())
                .estado(request.getEstado())
                .ciudad(ciudad)
                .build();

        Hospital saved = hospitalRepository.save(hospitalBuilder);
        return toResponse(saved);
    }

    /**
     * Obtiene la lista completa de todos los hospitales registrados en el sistema.
     *
     * <p>La operación es de solo lectura.</p>
     *
     * @return una lista de objetos {@link HospitalResponse} con la información
     *         de todos los hospitales. Si no hay hospitales registrados,
     *         la lista estará vacía
     */
    @Override
    @Transactional(readOnly = true)
    public List<HospitalResponse> getAllHospitals() {
        return hospitalRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la información de un hospital específico a partir de su código.
     *
     * <p>La operación es de solo lectura.</p>
     *
     * @param codigo código único del hospital que se desea consultar
     * @return un objeto {@link HospitalResponse} con la información del
     *         hospital solicitado
     * @throws EntityNotFoundException si no se encuentra un hospital con el
     *                                 código especificado
     */
    @Override
    @Transactional(readOnly = true)
    public HospitalResponse getHospitalByCodigo(String codigo) {
        Hospital hospital = validarHospital(codigo);
        return toResponse(hospital);
    }

    /**
     * Valida la existencia de una ciudad en la base de datos a partir de su código.
     *
     * <p>Si la ciudad no existe, se lanza una excepción para notificar
     * el error al llamador.</p>
     *
     * @param codigoCiudad código único de la ciudad a validar
     * @return la entidad {@link Ciudad} encontrada en la base de datos
     * @throws EntityNotFoundException si no se encuentra una ciudad con el
     *                                 código especificado
     */
    private Ciudad validarCiudad(String codigoCiudad) {
        return ciudadRepository.findById(codigoCiudad)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Ciudad no encontrada con código: " + codigoCiudad));
    }

    /**
     * Valida la existencia de un hospital en la base de datos a partir de su código.
     *
     * <p>Si el hospital no existe, se lanza una excepción para notificar
     * el error al llamador.</p>
     *
     * @param codigoHospital código único del hospital a validar
     * @return la entidad {@link Hospital} encontrada en la base de datos
     * @throws EntityNotFoundException si no se encuentra un hospital con el
     *                                 código especificado
     */
    private Hospital validarHospital(String codigoHospital) {
        return hospitalRepository.findById(codigoHospital)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Hospital no encontrado con código: " + codigoHospital));
    }
}