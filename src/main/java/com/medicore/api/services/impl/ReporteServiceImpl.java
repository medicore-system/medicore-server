package com.medicore.api.services.impl;

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
    @Transactional(readOnly = true) // Súper importante: Le dice a Hibernate que no haga "dirty checking", optimizando la memoria.
    public List<IngresosHospitalDTO> obtenerIngresosPorHospital(int anio) {
        
        int anioActual = LocalDate.now().getYear();
        
        // Regla de Negocio: No se pueden consultar reportes del futuro ni anteriores al año de fundación del sistema (ej. 2000)
        if (anio > anioActual || anio < 2000) {
            throw new IllegalArgumentException("El año de consulta debe estar entre 2000 y el año actual (" + anioActual + ").");
        }

        // Llamamos a nuestro Repositorio Personalizado. ¡Mira lo limpio que queda!
        return facturaRepository.obtenerIngresosPorHospitalYAnio(anio);
    }
}