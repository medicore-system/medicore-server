package com.medicore.api.services.impl;

import com.medicore.api.dtos.liquidacion.LiquidacionRequestDTO;
import com.medicore.api.dtos.liquidacion.LiquidacionResponseDTO;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Factura;
import com.medicore.api.entities.Servicio;
import com.medicore.api.entities.costos.Liquidacion;
import com.medicore.api.entities.costos.TarifaEps;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.mappers.LiquidacionMapper;
import com.medicore.api.repositories.FacturaRepository;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.repositories.costos.LiquidacionRepository;
import com.medicore.api.repositories.costos.TarifaEpsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link LiquidacionServiceImpl}.
 *
 * <p>Este servicio es el más crítico del sistema porque maneja
 * los cálculos financieros de las liquidaciones a las EPS.
 * Un error en estos cálculos tendría impacto monetario directo,
 * por lo que la cobertura de pruebas es prioritaria.</p>
 *
 * <p>Se validan los siguientes comportamientos:
 * <ul>
 *   <li>Cálculo correcto del total bruto, cobertura EPS y copago paciente.</li>
 *   <li>Manejo de la regla de negocio cuando no existen facturas pendientes.</li>
 *   <li>Validación de la existencia de la EPS.</li>
 *   <li>Cambio de estado válido e inválido de una liquidación.</li>
 * </ul></p>
 *
 * <p>Sigue el patrón AAA (Arrange, Act, Assert) y el principio de
 * Single Responsibility (cada test valida un único comportamiento).</p>
 */
class LiquidacionServiceImplTest {

    /**
     * Repositorio simulado de liquidaciones.
     */
    @Mock
    private LiquidacionRepository liquidacionRepository;

    /**
     * Repositorio simulado de facturas.
     */
    @Mock
    private FacturaRepository facturaRepository;

    /**
     * Repositorio simulado de tarifas EPS.
     */
    @Mock
    private TarifaEpsRepository tarifaEpsRepository;

    /**
     * Repositorio simulado de EPS.
     */
    @Mock
    private IEpsRepository epsRepository;

    /**
     * Mapper simulado para convertir entidades en DTOs de respuesta.
     */
    @Mock
    private LiquidacionMapper liquidacionMapper;

    /**
     * Instancia del servicio bajo prueba con los mocks inyectados.
     */
    @InjectMocks
    private LiquidacionServiceImpl liquidacionService;

    /**
     * Inicializa los mocks antes de cada prueba para garantizar
     * que las pruebas sean independientes entre sí.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifica que generarLiquidacion calcule correctamente el total bruto,
     * la cobertura de la EPS y el copago del paciente cuando se procesan
     * facturas con diferentes tarifas de cobertura.
     *
     * <p>Escenario:
     * <ul>
     *   <li>Factura 1: costo $100.000, servicio con 70% de cobertura.</li>
     *   <li>Factura 2: costo $50.000, servicio con 50% de cobertura.</li>
     * </ul></p>
     *
     * <p>Resultado esperado:
     * <ul>
     *   <li>Total bruto: $150.000</li>
     *   <li>Total cobertura EPS: $95.000 (70.000 + 25.000)</li>
     *   <li>Total copago paciente: $55.000 (150.000 - 95.000)</li>
     * </ul></p>
     */
    @Test
    void generarLiquidacionDebeCalcularTotalesCorrectamente() {
        // --- Patrón AAA ---

        // Arrange
        Eps eps = new Eps();
        eps.setCodigo("EPS001");
        eps.setNombre("Sura");

        Servicio servicio1 = new Servicio();
        servicio1.setCodigo("SERV001");

        Servicio servicio2 = new Servicio();
        servicio2.setCodigo("SERV002");

        Factura factura1 = Factura.builder()
                .codigo("FAC001")
                .costoTotal(new BigDecimal("100000.00"))
                .servicio(servicio1)
                .build();

        Factura factura2 = Factura.builder()
                .codigo("FAC002")
                .costoTotal(new BigDecimal("50000.00"))
                .servicio(servicio2)
                .build();

        TarifaEps tarifa1 = TarifaEps.builder()
                .servicio(servicio1)
                .porcentajeCobertura(new BigDecimal("70.00"))
                .build();

        TarifaEps tarifa2 = TarifaEps.builder()
                .servicio(servicio2)
                .porcentajeCobertura(new BigDecimal("50.00"))
                .build();

        LiquidacionRequestDTO request = new LiquidacionRequestDTO(
                "EPS001", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        when(epsRepository.findById("EPS001")).thenReturn(Optional.of(eps));
        when(facturaRepository.findByEpsCodigoAndFechaBetweenAndLiquidacionIsNull(
                "EPS001", request.fechaInicio(), request.fechaFin()))
                .thenReturn(List.of(factura1, factura2));
        when(tarifaEpsRepository.findByEpsCodigoAndEstadoTrue("EPS001"))
                .thenReturn(List.of(tarifa1, tarifa2));

        // ArgumentCaptor permite "capturar" el objeto que el servicio
        // pasa al método save(), para luego inspeccionar sus campos
        // y verificar que los cálculos sean correctos.
        ArgumentCaptor<Liquidacion> captor = ArgumentCaptor.forClass(Liquidacion.class);
        when(liquidacionRepository.save(captor.capture())).thenReturn(new Liquidacion());
        when(liquidacionMapper.toResponseDTO(any(Liquidacion.class)))
                .thenReturn(mock(LiquidacionResponseDTO.class));

        // Act
        liquidacionService.generarLiquidacion(request);

        // Assert
        Liquidacion liquidacionGuardada = captor.getValue();
        assertEquals(0, new BigDecimal("150000.00").compareTo(liquidacionGuardada.getTotalBruto()));
        assertEquals(0, new BigDecimal("95000.00").compareTo(liquidacionGuardada.getTotalCoberturaEps()));
        assertEquals(0, new BigDecimal("55000.00").compareTo(liquidacionGuardada.getTotalCopagoPaciente()));
        assertEquals("PENDIENTE", liquidacionGuardada.getEstado());
    }

    /**
     * Verifica que generarLiquidacion lance {@link IllegalArgumentException}
     * cuando no existen facturas pendientes en el rango de fechas indicado
     * para la EPS especificada.
     */
    @Test
    void generarLiquidacionDebeLanzarExcepcionSiNoHayFacturasPendientes() {
        // --- Patrón AAA ---

        // Arrange
        Eps eps = new Eps();
        eps.setCodigo("EPS001");

        LiquidacionRequestDTO request = new LiquidacionRequestDTO(
                "EPS001", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        when(epsRepository.findById("EPS001")).thenReturn(Optional.of(eps));
        when(facturaRepository.findByEpsCodigoAndFechaBetweenAndLiquidacionIsNull(
                any(), any(), any()))
                .thenReturn(Collections.emptyList());

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> liquidacionService.generarLiquidacion(request));

        assertTrue(exception.getMessage().contains("No hay facturas pendientes"));
        verify(liquidacionRepository, never()).save(any(Liquidacion.class));
    }

    /**
     * Verifica que generarLiquidacion lance {@link RecursoNoEncontradoException}
     * cuando el código de la EPS proporcionado no existe en la base de datos.
     */
    @Test
    void generarLiquidacionDebeLanzarExcepcionSiLaEpsNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        LiquidacionRequestDTO request = new LiquidacionRequestDTO(
                "EPS_FANTASMA", LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 31));

        when(epsRepository.findById("EPS_FANTASMA")).thenReturn(Optional.empty());

        // Act + Assert
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> liquidacionService.generarLiquidacion(request));

        assertTrue(exception.getMessage().contains("EPS no encontrada"));
        verify(facturaRepository, never())
                .findByEpsCodigoAndFechaBetweenAndLiquidacionIsNull(any(), any(), any());
    }

    /**
     * Verifica que cambiarEstado actualice correctamente el estado
     * de una liquidación existente cuando se le pasa un estado válido
     * (por ejemplo, "PAGADA").
     */
    @Test
    void cambiarEstadoDebeActualizarCorrectamenteAPagada() {
        // --- Patrón AAA ---

        // Arrange
        String codigo = "LIQ001";
        Liquidacion liquidacion = new Liquidacion();
        liquidacion.setCodigo(codigo);
        liquidacion.setEstado("PENDIENTE");

        when(liquidacionRepository.findById(codigo)).thenReturn(Optional.of(liquidacion));
        when(liquidacionRepository.save(any(Liquidacion.class))).thenReturn(liquidacion);
        when(liquidacionMapper.toResponseDTO(any(Liquidacion.class)))
                .thenReturn(mock(LiquidacionResponseDTO.class));

        // Act
        liquidacionService.cambiarEstado(codigo, "PAGADA");

        // Assert
        assertEquals("PAGADA", liquidacion.getEstado());
        verify(liquidacionRepository, times(1)).save(liquidacion);
    }

    /**
     * Verifica que cambiarEstado lance {@link IllegalArgumentException}
     * cuando se intenta asignar un estado que no es ni "PAGADA" ni "PENDIENTE".
     */
    @Test
    void cambiarEstadoDebeLanzarExcepcionConEstadoInvalido() {
        // --- Patrón AAA ---

        // Arrange
        String codigo = "LIQ001";
        Liquidacion liquidacion = new Liquidacion();
        liquidacion.setCodigo(codigo);
        liquidacion.setEstado("PENDIENTE");

        when(liquidacionRepository.findById(codigo)).thenReturn(Optional.of(liquidacion));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> liquidacionService.cambiarEstado(codigo, "ANULADA"));

        assertTrue(exception.getMessage().contains("Estado no permitido"));
        verify(liquidacionRepository, never()).save(any(Liquidacion.class));
    }

    /**
     * Verifica que cambiarEstado lance {@link RecursoNoEncontradoException}
     * cuando se intenta modificar una liquidación cuyo código no existe.
     */
    @Test
    void cambiarEstadoDebeLanzarExcepcionSiLaLiquidacionNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        String codigoInexistente = "LIQ999";
        when(liquidacionRepository.findById(codigoInexistente)).thenReturn(Optional.empty());

        // Act + Assert
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> liquidacionService.cambiarEstado(codigoInexistente, "PAGADA"));

        assertTrue(exception.getMessage().contains("Liquidación no encontrada"));
        verify(liquidacionRepository, never()).save(any(Liquidacion.class));
    }
}
