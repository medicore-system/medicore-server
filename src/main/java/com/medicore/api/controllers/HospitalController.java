package com.medicore.api.controllers;

import com.medicore.api.dtos.areainterna.AreaInternaRequest;
import com.medicore.api.dtos.areainterna.AreaInternaResponse;
import com.medicore.api.dtos.areainterna.AreaInternaUpdateRequest;
import com.medicore.api.dtos.hospital.HospitalRequest;
import com.medicore.api.dtos.hospital.HospitalResponse;
import com.medicore.api.dtos.hospital.HospitalUpdateRequest;
import com.medicore.api.services.IHospitalAreaInternaService;
import com.medicore.api.services.IHospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas
 * con los hospitales y sus áreas internas en el sistema MediCore.
 *
 * <p>Expone endpoints para la creación, actualización y consulta de hospitales,
 * así como para la administración de las áreas internas asociadas a cada hospital.</p>
 *
 * <p>Ruta base: {@code /hospitals}</p>
 *
 * @author Juan Sebastián López Guzmán
 * @author Cristian Camilo Salazar Arenas
 * @see IHospitalService
 * @see IHospitalAreaInternaService
 * @see HospitalRequest
 * @see HospitalResponse
 * @see HospitalUpdateRequest
 * @see AreaInternaRequest
 * @see AreaInternaResponse
 * @see AreaInternaUpdateRequest
 */
@RestController
@RequestMapping("/hospitals")
@RequiredArgsConstructor
public class HospitalController {

    /** Servicio para la lógica de negocio de hospitales. */
    private final IHospitalService hospitalService;

    /** Servicio para la lógica de negocio de áreas internas de hospitales. */
    private final IHospitalAreaInternaService hospitalAreaInternaService;

    /**
     * Crea un nuevo hospital en el sistema.
     *
     * <p>Recibe los datos del hospital en el cuerpo de la petición,
     * los valida y delega la creación al servicio correspondiente.</p>
     *
     * @param request objeto con los datos necesarios para crear el hospital,
     *                validado con las anotaciones de Jakarta Bean Validation
     * @return una respuesta HTTP con estado {@code 201 Created} y el
     *         objeto {@link HospitalResponse} con la información del
     *         hospital recién creado en el cuerpo de la respuesta
     */
    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(
            @Valid @RequestBody HospitalRequest request) {
        HospitalResponse response = hospitalService.createHospital(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza la información de un hospital existente identificado por su ID.
     *
     * <p>Recibe el ID del hospital como parámetro de ruta y los nuevos datos
     * en el cuerpo de la petición. La actualización se delega al servicio
     * de hospitales.</p>
     *
     * @param request objeto con los datos actualizados del hospital,
     *                validado con Jakarta Bean Validation
     * @param id      identificador único del hospital a actualizar,
     *                proporcionado en la ruta de la URL
     * @return una respuesta HTTP con estado {@code 200 OK} y el objeto
     *         {@link HospitalResponse} con la información actualizada
     *         del hospital en el cuerpo de la respuesta
     */
    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponse> updateHospital(
            @Valid @RequestBody HospitalUpdateRequest request,
            @PathVariable String id) {
        HospitalResponse response = hospitalService.updateHospital(request, id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /**
     * Obtiene la lista completa de todos los hospitales registrados en el sistema.
     *
     * @return una respuesta HTTP con estado {@code 200 OK} y una lista de
     *         objetos {@link HospitalResponse} con la información de todos
     *         los hospitales en el cuerpo de la respuesta. Si no hay
     *         hospitales registrados, la lista estará vacía
     */
    @GetMapping
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() {
        List<HospitalResponse> hospitals = hospitalService.getAllHospitals();
        return ResponseEntity.ok(hospitals);
    }

    /**
     * Obtiene la información de un hospital específico a partir de su código.
     *
     * @param codigo identificador único del hospital que se desea consultar,
     *               proporcionado como parámetro de ruta {@code id}
     * @return una respuesta HTTP con estado {@code 200 OK} y el objeto
     *         {@link HospitalResponse} con la información del hospital
     *         solicitado en el cuerpo de la respuesta
     */
    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> getHospitalByCodigo(
            @PathVariable("id") String codigo) {
        HospitalResponse response = hospitalService.getHospitalByCodigo(codigo);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene la lista de áreas internas asociadas a un hospital específico.
     *
     * @param id identificador único del hospital del cual se desean
     *           consultar las áreas internas, proporcionado como
     *           parámetro de ruta
     * @return una respuesta HTTP con estado {@code 200 OK} y una lista de
     *         objetos {@link AreaInternaResponse} con la información de
     *         las áreas internas del hospital en el cuerpo de la respuesta.
     *         Si el hospital no tiene áreas registradas, la lista estará vacía
     */
    @GetMapping("/{id}/areas")
    public ResponseEntity<List<AreaInternaResponse>> getAreasByHospital(
            @PathVariable String id) {
        List<AreaInternaResponse> areas = hospitalAreaInternaService.getAreasByHospital(id);
        return ResponseEntity.ok(areas);
    }

    /**
     * Crea una nueva área interna para un hospital específico.
     *
     * <p>Asocia el área interna al hospital identificado por el ID
     * proporcionado en la ruta y delega la creación al servicio de
     * áreas internas.</p>
     *
     * @param id      identificador único del hospital al que se asociará
     *                la nueva área interna, proporcionado como parámetro
     *                de ruta
     * @param request objeto con los datos necesarios para crear el área
     *                interna, validado con Jakarta Bean Validation
     * @return una respuesta HTTP con estado {@code 201 Created} y el
     *         objeto {@link AreaInternaResponse} con la información del
     *         área interna recién creada en el cuerpo de la respuesta
     */
    @PostMapping("/{id}/areas")
    public ResponseEntity<AreaInternaResponse> createArea(
            @PathVariable String id,
            @Valid @RequestBody AreaInternaRequest request) {
        AreaInternaResponse response = hospitalAreaInternaService.createArea(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza la información de un área interna específica dentro de un hospital.
     *
     * <p>Recibe el ID del hospital y el ID del área interna como parámetros
     * de ruta, junto con los nuevos datos en el cuerpo de la petición.
     * La actualización se delega al servicio de áreas internas.</p>
     *
     * @param id      identificador único del hospital que contiene el área
     *                interna a actualizar, proporcionado como parámetro
     *                de ruta
     * @param areaId  identificador único del área interna que se desea
     *                actualizar, proporcionado como parámetro de ruta
     * @param request objeto con los datos actualizados del área interna,
     *                validado con Jakarta Bean Validation
     * @return una respuesta HTTP con estado {@code 200 OK} y el objeto
     *         {@link AreaInternaResponse} con la información actualizada
     *         del área interna en el cuerpo de la respuesta
     */
    @PutMapping("/{id}/areas/{areaId}")
    public ResponseEntity<AreaInternaResponse> updateArea(
            @PathVariable String id,
            @PathVariable String areaId,
            @Valid @RequestBody AreaInternaUpdateRequest request) {
        AreaInternaResponse response = hospitalAreaInternaService.updateArea(id, areaId, request);
        return ResponseEntity.ok(response);
    }
}