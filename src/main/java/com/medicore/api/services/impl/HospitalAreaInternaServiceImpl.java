package com.medicore.api.services.impl;

import com.medicore.api.dtos.areainterna.AreaInternaRequest;
import com.medicore.api.dtos.areainterna.AreaInternaResponse;
import com.medicore.api.dtos.areainterna.AreaInternaUpdateRequest;
import com.medicore.api.entities.areainterna.AreaInterna;
import com.medicore.api.entities.hospital.Hospital;
import com.medicore.api.entities.areainterna.HospitalAreaInterna;
import com.medicore.api.repositories.areinterna.AreaInternaRepository;
import com.medicore.api.repositories.areinterna.HospitalAreaInternaRepository;
import com.medicore.api.repositories.hospital.HospitalRepository;
import com.medicore.api.services.IHospitalAreaInternaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio {@link IHospitalAreaInternaService} que gestiona
 * la lógica de negocio relacionada con las áreas internas de los hospitales
 * en el sistema MediCore.
 *
 * <p>Esta clase proporciona la implementación de los métodos definidos en la
 * interfaz {@code IHospitalAreaInternaService}, incluyendo la consulta, creación
 * y actualización de áreas internas asociadas a un hospital específico.</p>
 *
 * <p>Utiliza los repositorios {@link HospitalAreaInternaRepository},
 * {@link HospitalRepository} y {@link AreaInternaRepository} para acceder
 * a la capa de persistencia. Todas las operaciones de escritura son transaccionales.</p>
 *
 * <p>Se apoya en Lombok para la inyección de dependencias por constructor
 * a través de la anotación {@link RequiredArgsConstructor}.</p>
 *
 * @author Cristian Camilo Salazar Arenas
 * @see IHospitalAreaInternaService
 * @see HospitalAreaInternaRepository
 * @see HospitalRepository
 * @see AreaInternaRepository
 */
@Service
@RequiredArgsConstructor
public class HospitalAreaInternaServiceImpl implements IHospitalAreaInternaService {

    /** Repositorio para operaciones de persistencia de áreas internas de hospital. */
    private final HospitalAreaInternaRepository hospitalAreaInternaRepository;

    /** Repositorio para operaciones de persistencia de hospitales. */
    private final HospitalRepository hospitalRepository;

    /** Repositorio para operaciones de persistencia de áreas internas base. */
    private final AreaInternaRepository areaInternaRepository;

    /**
     * Obtiene la lista de áreas internas asociadas a un hospital específico.
     *
     * <p>Valida la existencia del hospital antes de realizar la consulta.
     * La operación es de solo lectura.</p>
     *
     * @param codigoHospital código único del hospital del cual se desean
     *                       consultar las áreas internas
     * @return una lista de objetos {@link AreaInternaResponse} con la información
     *         de las áreas internas del hospital. Si el hospital no tiene áreas
     *         registradas, la lista estará vacía
     * @throws EntityNotFoundException si no se encuentra un hospital con el
     *                                 código especificado
     */
    @Override
    @Transactional(readOnly = true)
    public List<AreaInternaResponse> getAreasByHospital(String codigoHospital) {
        validarHospital(codigoHospital);
        return hospitalAreaInternaRepository.findByHospitalCodigo(codigoHospital)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva área interna y la asocia a un hospital específico.
     *
     * <p>Valida la existencia del hospital y del área interna base antes de
     * realizar la creación. La operación es transaccional.</p>
     *
     * @param codigoHospital código único del hospital al que se asociará
     *                       la nueva área interna
     * @param request        objeto con los datos necesarios para crear el
     *                       área interna, incluyendo el código del área
     *                       interna base
     * @return un objeto {@link AreaInternaResponse} con la información del
     *         área interna recién creada
     * @throws EntityNotFoundException si no se encuentra un hospital con el
     *                                 código especificado o si no se encuentra
     *                                 un área interna base con el código
     *                                 proporcionado en el request
     */
    @Override
    @Transactional
    public AreaInternaResponse createArea(String codigoHospital, AreaInternaRequest request) {
        Hospital hospital = validarHospital(codigoHospital);
        AreaInterna areaInterna = areaInternaRepository.findById(request.getCodigoAreaInterna())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Área interna no encontrada con código: " + request.getCodigoAreaInterna()));

        HospitalAreaInterna area = new HospitalAreaInterna();
        area.setCodigo(request.getCodigo());
        area.setNombre(request.getNombre());
        area.setDescripcion(request.getDescripcion());
        area.setHospital(hospital);
        area.setAreaInterna(areaInterna);

        HospitalAreaInterna saved = hospitalAreaInternaRepository.save(area);
        return toResponse(saved);
    }

    /**
     * Actualiza la información de un área interna existente asociada a un hospital.
     *
     * <p>Valida la existencia del hospital, del área a actualizar y del área
     * interna base antes de realizar la modificación. La operación es transaccional.</p>
     *
     * @param codigoHospital código único del hospital que contiene el área
     *                       interna a actualizar
     * @param codigoArea     código único del área interna que se desea actualizar
     * @param request        objeto con los datos actualizados del área interna,
     *                       incluyendo el nuevo código del área interna base
     * @return un objeto {@link AreaInternaResponse} con la información actualizada
     *         del área interna
     * @throws EntityNotFoundException si no se encuentra un hospital con el
     *                                 código especificado, si no se encuentra
     *                                 el área a actualizar o si no se encuentra
     *                                 un área interna base con el código
     *                                 proporcionado en el request
     */
    @Override
    @Transactional
    public AreaInternaResponse updateArea(String codigoHospital, String codigoArea, AreaInternaUpdateRequest request) {
        validarHospital(codigoHospital);
        HospitalAreaInterna area = hospitalAreaInternaRepository.findById(codigoArea)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Área no encontrada con código: " + codigoArea));

        AreaInterna areaInterna = areaInternaRepository.findById(request.getCodigoAreaInterna())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Área interna no encontrada con código: " + request.getCodigoAreaInterna()));

        area.setNombre(request.getNombre());
        area.setDescripcion(request.getDescripcion());
        area.setAreaInterna(areaInterna);

        HospitalAreaInterna saved = hospitalAreaInternaRepository.save(area);
        return toResponse(saved);
    }

    /**
     * Convierte una entidad {@link HospitalAreaInterna} a su correspondiente
     * DTO de respuesta {@link AreaInternaResponse}.
     *
     * @param area la entidad que se desea convertir a DTO
     * @return un objeto {@link AreaInternaResponse} con los datos mapeados
     *         desde la entidad, incluyendo el código y nombre del área
     *         interna base asociada
     */
    private AreaInternaResponse toResponse(HospitalAreaInterna area) {
        return AreaInternaResponse.builder()
                .codigo(area.getCodigo())
                .nombre(area.getNombre())
                .descripcion(area.getDescripcion())
                .codigoAreaInterna(area.getAreaInterna().getCodigo())
                .nombreAreaInterna(area.getAreaInterna().getNombre())
                .build();
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