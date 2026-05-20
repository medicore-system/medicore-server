package com.medicore.api.services;
import com.medicore.api.dtos.factura.FacturaCajaDTO;
import java.util.List;

public interface ICajaService {
    List<FacturaCajaDTO> buscarFacturasPendientesPorPaciente(String documentoPaciente);
    byte[] procesarPagoYGenerarRecibo(String codigoFactura);
}