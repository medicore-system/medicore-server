package com.medicore.api.services.impl;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.EstadoCarteraDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.dtos.reportes.ProductividadMedicoDTO;
import com.medicore.api.repositories.FacturaRepository;
import com.medicore.api.repositories.cita.ICitaRepository;
import com.medicore.api.repositories.costos.LiquidacionRepository;
import com.medicore.api.services.IReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final FacturaRepository facturaRepository;
    private final ICitaRepository citaRepository;
    private final LiquidacionRepository liquidacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<IngresosHospitalDTO> obtenerIngresosPorHospital(int anio) {

        int anioActual = LocalDate.now().getYear();

        if (anio > anioActual || anio < 2000) {
            throw new IllegalArgumentException(
                    "El año de consulta debe estar entre 2000 y el año actual (" + anioActual + ").");
        }

        return facturaRepository.obtenerIngresosPorHospitalYAnio(anio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngresosEspecialidadDTO> obtenerIngresosPorEspecialidad(int anio) {
        int anioActual = LocalDate.now().getYear();

        if (anio > anioActual || anio < 2000) {
            throw new IllegalArgumentException(
                    "El año de consulta debe estar entre 2000 y el año actual (" + anioActual + ").");
        }

        return facturaRepository.obtenerIngresosPorEspecialidadYAnio(anio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionesEpsDTO> obtenerAtencionesPorEps(int anio) {
        int anioActual = LocalDate.now().getYear();

        if (anio > anioActual || anio < 2000) {
            throw new IllegalArgumentException(
                    "El año de consulta debe estar entre 2000 y el año actual (" + anioActual + ").");
        }

        return facturaRepository.obtenerAtencionesPorEpsYAnio(anio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductividadMedicoDTO> obtenerProductividadMedica(int anio, int mes) {
        int anioActual = LocalDate.now().getYear();

        if (anio > anioActual || anio < 2000) {
            throw new IllegalArgumentException("El año de consulta es inválido.");
        }
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }

        return citaRepository.obtenerProductividadMedicaPorMes(anio, mes);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstadoCarteraDTO> obtenerEstadoCartera() {
        return liquidacionRepository.obtenerEstadoCarteraPorEps();
    }
}