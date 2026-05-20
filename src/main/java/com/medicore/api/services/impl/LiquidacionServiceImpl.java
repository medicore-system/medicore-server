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

                Eps eps = epsRepository.findById(request.codigoEps())
                                .orElseThrow(() -> new RecursoNoEncontradoException(
                                                "EPS no encontrada: " + request.codigoEps()));

                List<Factura> facturasPendientes = facturaRepository.findByEpsCodigoAndFechaBetweenAndLiquidacionIsNull(
                                eps.getCodigo(), request.fechaInicio(), request.fechaFin());

                if (facturasPendientes.isEmpty()) {
                        throw new IllegalArgumentException(
                                        "No hay facturas pendientes de cobro para esta EPS en el rango de fechas seleccionado.");
                }

                Map<String, BigDecimal> mapaTarifas = tarifaEpsRepository.findByEpsCodigoAndEstadoTrue(eps.getCodigo())
                                .stream()
                                .collect(Collectors.toMap(
                                                tarifa -> tarifa.getServicio().getCodigo(),
                                                TarifaEps::getPorcentajeCobertura));

                BigDecimal totalBruto = BigDecimal.ZERO;
                BigDecimal totalCoberturaEps = BigDecimal.ZERO;

                for (Factura factura : facturasPendientes) {
                        totalBruto = totalBruto.add(factura.getCostoTotal());

                        String codigoServicio = factura.getServicio().getCodigo();

                        BigDecimal porcentajeCobertura = mapaTarifas.getOrDefault(codigoServicio, BigDecimal.ZERO);

                        BigDecimal cobertura = factura.getCostoTotal()
                                        .multiply(porcentajeCobertura)
                                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

                        totalCoberturaEps = totalCoberturaEps.add(cobertura);
                }

                BigDecimal totalCopagoPaciente = totalBruto.subtract(totalCoberturaEps);

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

                Liquidacion liquidacionGuardada = liquidacionRepository.save(nuevaLiquidacion);

                facturasPendientes.forEach(f -> f.setLiquidacion(liquidacionGuardada));
                facturaRepository.saveAll(facturasPendientes);

                return liquidacionMapper.toResponseDTO(liquidacionGuardada);
        }

        @Override
        @Transactional(readOnly = true) // readOnly es excelente práctica para rendimiento en consultas GET
        public List<LiquidacionResponseDTO> obtenerTodas() {

                List<Liquidacion> liquidaciones = liquidacionRepository.findAll();

                return liquidaciones.stream()
                                .map(liquidacionMapper::toResponseDTO)
                                .collect(Collectors.toList());
        }
}