package com.medicore.api.services;

import com.medicore.api.dtos.areainterna.AreaInternaRequest;
import com.medicore.api.dtos.areainterna.AreaInternaResponse;
import com.medicore.api.dtos.areainterna.AreaInternaUpdateRequest;

import java.util.List;

/**
 * Interfaz que define el contrato para el servicio de gestión de áreas
 * internas de hospitales en el sistema MediCore.
 *
 * <p>Declara los métodos necesarios para consultar, crear y actualizar
 * las áreas internas asociadas a un hospital específico. Las clases que
 * implementen esta interfaz deben proporcionar la lógica de negocio
 * correspondiente a cada operación.</p>
 *
 * @author Cristian Camilo Salazar Arenas
 * @see com.medicore.api.services.impl.HospitalAreaInternaServiceImpl
 * @see AreaInternaRequest
 * @see AreaInternaResponse
 * @see AreaInternaUpdateRequest
 */
public interface IHospitalAreaInternaService {

    /**
     * Obtiene la lista de áreas internas asociadas a un hospital específico.
     *
     * @param codigoHospital código único del hospital del cual se desean
     *                       consultar las áreas internas
     * @return una lista de objetos {@link AreaInternaResponse} con la información
     *         de las áreas internas del hospital. Si el hospital no tiene áreas
     *         registradas, la lista estará vacía
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra
     *                                                      un hospital con el
     *                                                      código especificado
     */
    List<AreaInternaResponse> getAreasByHospital(String codigoHospital);

    /**
     * Crea una nueva área interna y la asocia a un hospital específico.
     *
     * @param codigoHospital código único del hospital al que se asociará
     *                       la nueva área interna
     * @param request        objeto con los datos necesarios para crear el
     *                       área interna, incluyendo el código del área
     *                       interna base
     * @return un objeto {@link AreaInternaResponse} con la información del
     *         área interna recién creada
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra
     *                                                      un hospital con el
     *                                                      código especificado
     *                                                      o si no se encuentra
     *                                                      un área interna base
     *                                                      con el código
     *                                                      proporcionado
     */
    AreaInternaResponse createArea(String codigoHospital, AreaInternaRequest request);

    /**
     * Actualiza la información de un área interna existente asociada a un hospital.
     *
     * @param codigoHospital código único del hospital que contiene el área
     *                       interna a actualizar
     * @param codigoArea     código único del área interna que se desea actualizar
     * @param request        objeto con los datos actualizados del área interna,
     *                       incluyendo el nuevo código del área interna base
     * @return un objeto {@link AreaInternaResponse} con la información actualizada
     *         del área interna
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra
     *                                                      un hospital con el
     *                                                      código especificado,
     *                                                      si no se encuentra
     *                                                      el área a actualizar
     *                                                      o si no se encuentra
     *                                                      un área interna base
     *                                                      con el código
     *                                                      proporcionado
     */
    AreaInternaResponse updateArea(String codigoHospital, String codigoArea, AreaInternaUpdateRequest request);
}