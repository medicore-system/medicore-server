package com.medicore.api.services.impl;

import com.medicore.api.dtos.reportes.AtencionesEpsDTO;
import com.medicore.api.dtos.reportes.IngresosEspecialidadDTO;
import com.medicore.api.dtos.reportes.IngresosHospitalDTO;
import com.medicore.api.repositories.FacturaRepository;
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
}