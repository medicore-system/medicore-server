package com.medicore.api.services;

import com.medicore.api.dtos.servicio.ServicioDetalleResponse;
import com.medicore.api.dtos.servicio.ServicioRequest;
import com.medicore.api.dtos.servicio.ServicioResponse;
import com.medicore.api.dtos.servicio.TipoServicioResponse;

import java.util.List;

public interface IServicioService {

    List<ServicioResponse> listarServicios();

    List<ServicioResponse> buscarServicios(String termino);

    ServicioDetalleResponse obtenerDetalleServicio(String codigo);

    ServicioResponse crearServicio(ServicioRequest request);

    ServicioResponse editarServicio(String codigo, ServicioRequest request);

    void inactivarServicio(String codigo);

    List<TipoServicioResponse> listarTiposServicio();
}