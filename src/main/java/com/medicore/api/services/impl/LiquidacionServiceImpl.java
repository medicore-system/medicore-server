package com.medicore.api.services.impl;

import com.medicore.api.dtos.liquidacion.LiquidacionRequestDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.costos.Liquidacion;
import com.medicore.api.entities.costos.TarifaEps;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.mappers.LiquidacionMapper;
import com.medicore.api.repositories.FacturaRepository;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.repositories.costos.LiquidacionRepository;
import com.medicore.api.repositories.costos.TarifaEpsRepository;
import com.medicore.api.services.ILiquidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LiquidacionServiceImpl implements ILiquidacionService {

    private final LiquidacionRepository liquidacionRepository;
    private final FacturaRepository facturaRepository;
    private final TarifaEpsRepository tarifaEpsRepository;
    private final IEpsRepository epsRepository;
    private final LiquidacionMapper liquidacionMapper;

    @Override
    @Transactional
    public LiquidacionResponseDTO generarLiquidacion(LiquidacionRequestDTO request) {
        
        // 1. Validar EPS
        Eps eps = epsRepository.findById(request.codigoEps())
                .orElseThrow(() -> new RecursoNoEncontradoException("EPS no encontrada: " + request.codigoEps()));

        // 2. Buscar facturas pendientes en el rango de fechas
        List<Factura> facturasPendientes = facturaRepository.findByEpsCodigoAndFechaBetweenAndLiquidacionIsNull(
                eps.getCodigo(), request.fechaInicio(), request.fechaFin()
        );

        if (facturasPendientes.isEmpty()) {
            throw new IllegalArgumentException("No hay facturas pendientes de cobro para esta EPS en el rango de fechas seleccionado.");
        }

        // 3. Optimización: Cargar mapa de tarifas de la EPS en memoria (Key: codigoServicio, Value: porcentaje)
        Map<String, BigDecimal> mapaTarifas = tarifaEpsRepository.findByEpsCodigoAndEstadoTrue(eps.getCodigo())
                .stream()
                .collect(Collectors.toMap(
                        tarifa -> tarifa.getServicio().getCodigo(),
                        TarifaEps::getPorcentajeCobertura
                ));

        // 4. Variables para acumular la matemática financiera
        BigDecimal totalBruto = BigDecimal.ZERO;
        BigDecimal totalCoberturaEps = BigDecimal.ZERO;

        // 5. Procesar cada factura
        for (Factura factura : facturasPendientes) {
            totalBruto = totalBruto.add(factura.getCostoTotal());

            // Buscar si hay una tarifa pactada para el servicio de esta factura
            String codigoServicio = factura.getServicio().getCodigo();
            
            // Si no hay tarifa configurada (no la encontramos en el mapa), asumimos 0% de cobertura (todo lo paga el paciente)
            BigDecimal porcentajeCobertura = mapaTarifas.getOrDefault(codigoServicio, BigDecimal.ZERO);

            // Fórmula: Cobertura = CostoTotal * (Porcentaje / 100)
            BigDecimal cobertura = factura.getCostoTotal()
                    .multiply(porcentajeCobertura)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

            totalCoberturaEps = totalCoberturaEps.add(cobertura);
        }

        // Fórmula Copago: Lo que no paga la EPS, lo asume el paciente
        BigDecimal totalCopagoPaciente = totalBruto.subtract(totalCoberturaEps);

        // 6. Generar el registro de Liquidación
        Liquidacion nuevaLiquidacion = Liquidacion.builder()
                .eps(eps)
                .fechaInicio(request.fechaInicio())
                .fechaFin(request.fechaFin())
                .totalBruto(totalBruto)
                .totalCoberturaEps(totalCoberturaEps)
                .totalCopagoPaciente(totalCopagoPaciente)
                .estado("PENDIENTE") // Se cobrará posteriormente
                .facturas(facturasPendientes)
                .build();

        // 7. Guardar la liquidación en base de datos (PostgreSQL generará el código 'LIQ-X')
        Liquidacion liquidacionGuardada = liquidacionRepository.save(nuevaLiquidacion);

        // 8. Vincular las facturas a esta liquidación
        facturasPendientes.forEach(f -> f.setLiquidacion(liquidacionGuardada));
        facturaRepository.saveAll(facturasPendientes);

        // 9. Retornar el resumen al Frontend
        return liquidacionMapper.toResponseDTO(liquidacionGuardada);
    }
}