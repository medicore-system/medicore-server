package com.medicore.api.services;

import com.medicore.api.dtos.hospital.HospitalRequest;
import com.medicore.api.dtos.hospital.HospitalResponse;
import com.medicore.api.dtos.hospital.HospitalUpdateRequest;

import java.util.List;

/**
 * Interfaz que define el contrato para el servicio de gestión de hospitales
 * en el sistema MediCore.
 *
 * <p>Declara los métodos necesarios para crear, actualizar y consultar
 * hospitales. Las clases que implementen esta interfaz deben proporcionar
 * la lógica de negocio correspondiente a cada operación.</p>
 *
 * @author Juan Sebastián López Guzmán
 * @author Cristian Camilo Salazar Arenas
 * @see com.medicore.api.services.impl.HospitalServiceImpl
 * @see HospitalRequest
 * @see HospitalResponse
 * @see HospitalUpdateRequest
 */
public interface IHospitalService {

    /**
     * Crea un nuevo hospital en el sistema.
     *
     * @param request objeto con los datos necesarios para crear el hospital,
     *                incluyendo el código, nombre, dirección, teléfono y
     *                código de la ciudad donde se ubica
     * @return un objeto {@link HospitalResponse} con la información del
     *         hospital recién creado, incluyendo los datos de la ciudad
     *         asociada
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra
     *                                                      una ciudad con el
     *                                                      código especificado
     *                                                      en el request
     */
    HospitalResponse createHospital(HospitalRequest request);

    /**
     * Actualiza la información de un hospital existente identificado por su ID.
     *
     * @param request objeto con los datos actualizados del hospital,
     *                incluyendo el nuevo nombre, dirección, teléfono,
     *                estado y código de ciudad
     * @param id      identificador único del hospital que se desea actualizar
     * @return un objeto {@link HospitalResponse} con la información
     *         actualizada del hospital
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra
     *                                                      un hospital con el
     *                                                      ID especificado o
     *                                                      si no se encuentra
     *                                                      una ciudad con el
     *                                                      código proporcionado
     *                                                      en el request
     */
    HospitalResponse updateHospital(HospitalUpdateRequest request, String id);

    /**
     * Obtiene la lista completa de todos los hospitales registrados en el sistema.
     *
     * @return una lista de objetos {@link HospitalResponse} con la información
     *         de todos los hospitales. Si no hay hospitales registrados,
     *         la lista estará vacía
     */
    List<HospitalResponse> getAllHospitals();

    /**
     * Obtiene la información de un hospital específico a partir de su código.
     *
     * @param codigo código único del hospital que se desea consultar
     * @return un objeto {@link HospitalResponse} con la información del
     *         hospital solicitado, incluyendo los datos de la ciudad
     *         asociada
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra
     *                                                      un hospital con el
     *                                                      código especificado
     */
    HospitalResponse getHospitalByCodigo(String codigo);
}