package com.medicore.api.services;

import com.medicore.api.dtos.servicio.ServicioDetalleResponse;
import com.medicore.api.dtos.servicio.ServicioRequest;
import com.medicore.api.dtos.servicio.ServicioResponse;
import com.medicore.api.dtos.servicio.TipoServicioResponse;

import java.util.List;

/**
 * Contrato principal para la gestión de servicios médicos dentro del sistema MediCore.
 *
 * <p>
 * Esta interfaz define las operaciones disponibles para administrar servicios médicos,
 * incluyendo su creación, consulta, actualización, inactivación y consulta de tipos.
 * </p>
 *
 * <p>
 * La implementación de esta interfaz debe encargarse de aplicar las reglas de negocio
 * necesarias, tales como validar duplicados, verificar la existencia del tipo de servicio
 * y asociar opcionalmente un historial clínico.
 * </p>
 */
public interface IServicioService {

    /**
     * Lista todos los servicios médicos registrados en el sistema.
     *
     * @return lista de servicios médicos ordenados según la lógica definida en el repositorio.
     */
    List<ServicioResponse> listarServicios();

    /**
     * Busca servicios médicos usando un término de búsqueda.
     *
     * <p>
     * Si el término es nulo o está vacío, se deben retornar todos los servicios.
     * La búsqueda puede aplicarse sobre campos como código, nombre o descripción,
     * según la implementación del repositorio.
     * </p>
     *
     * @param termino texto utilizado para filtrar servicios.
     * @return lista de servicios que coinciden con el criterio de búsqueda.
     */
    List<ServicioResponse> buscarServicios(String termino);

    /**
     * Obtiene el detalle completo de un servicio médico.
     *
     * <p>
     * El detalle puede incluir información del historial clínico asociado y las facturas
     * relacionadas con el servicio.
     * </p>
     *
     * @param codigo código único del servicio.
     * @return información detallada del servicio.
     */
    ServicioDetalleResponse obtenerDetalleServicio(String codigo);

    /**
     * Crea un nuevo servicio médico.
     *
     * <p>
     * La implementación debe validar que no exista otro servicio con el mismo nombre,
     * verificar el tipo de servicio y generar el código correspondiente.
     * </p>
     *
     * @param request datos necesarios para crear el servicio.
     * @return información del servicio creado.
     */
    ServicioResponse crearServicio(ServicioRequest request);

    /**
     * Edita la información de un servicio médico existente.
     *
     * <p>
     * La implementación debe validar que el servicio exista y que el nuevo nombre
     * no esté siendo utilizado por otro servicio.
     * </p>
     *
     * @param codigo código único del servicio que se desea editar.
     * @param request nuevos datos del servicio.
     * @return información actualizada del servicio.
     */
    ServicioResponse editarServicio(String codigo, ServicioRequest request);

    /**
     * Inactiva un servicio médico existente.
     *
     * <p>
     * Esta operación no elimina físicamente el registro de la base de datos.
     * En su lugar, cambia el estado del servicio a inactivo.
     * </p>
     *
     * @param codigo código único del servicio que se desea inactivar.
     */
    void inactivarServicio(String codigo);

    /**
     * Lista los tipos de servicio médico disponibles.
     *
     * @return lista de tipos de servicio registrados.
     */
    List<TipoServicioResponse> listarTiposServicio();
}