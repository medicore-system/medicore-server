package com.medicore.api.services.impl;

import com.medicore.api.dtos.tarifa.TarifaEpsRequestDTO;
import com.medicore.api.dtos.tarifa.TarifaEpsResponseDTO;
import com.medicore.api.entities.Eps;
import com.medicore.api.entities.Servicio;
import com.medicore.api.entities.costos.TarifaEps;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import com.medicore.api.mappers.TarifaEpsMapper;
import com.medicore.api.repositories.IEpsRepository;
import com.medicore.api.repositories.IServicioRepository;
import com.medicore.api.repositories.costos.TarifaEpsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link TarifaEpsServiceImpl}.
 *
 * <p>Verifica las reglas de negocio relacionadas con la
 * parametrización de tarifas de cobertura por EPS:
 * existencia de EPS y servicio, no duplicación de tarifas
 * y creación válida de nuevas tarifas.</p>
 *
 * <p>Se aplica el patrón AAA (Arrange, Act, Assert)
 * y el principio de Single Responsibility (cada test
 * valida una única regla de negocio).</p>
 */
class TarifaEpsServiceImplTest {

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
     * Repositorio simulado de servicios médicos.
     */
    @Mock
    private IServicioRepository servicioRepository;

    /**
     * Mapper simulado para transformar entidades a DTOs de respuesta.
     */
    @Mock
    private TarifaEpsMapper tarifaEpsMapper;

    /**
     * Instancia del servicio bajo prueba con los mocks inyectados.
     */
    @InjectMocks
    private TarifaEpsServiceImpl tarifaEpsService;

    /**
     * Inicializa los mocks antes de cada prueba para garantizar
     * aislamiento total entre pruebas.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifica que al intentar crear una tarifa con un código de EPS
     * que no existe, el sistema lance {@link RecursoNoEncontradoException}.
     */
    @Test
    void crearTarifaDebeLanzarExcepcionSiLaEpsNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        TarifaEpsRequestDTO request = new TarifaEpsRequestDTO(
                "EPS_INEXISTENTE", "SERV001", new BigDecimal("70.00"));

        when(epsRepository.findById("EPS_INEXISTENTE")).thenReturn(Optional.empty());

        // Act + Assert
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> tarifaEpsService.crearTarifa(request));

        assertTrue(exception.getMessage().contains("EPS no encontrada"));
        verify(tarifaEpsRepository, never()).save(any(TarifaEps.class));
    }

    /**
     * Verifica que al intentar crear una tarifa con un código de servicio
     * que no existe, el sistema lance {@link RecursoNoEncontradoException}.
     */
    @Test
    void crearTarifaDebeLanzarExcepcionSiElServicioNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        Eps eps = new Eps();
        eps.setCodigo("EPS001");
        eps.setNombre("Sura");

        TarifaEpsRequestDTO request = new TarifaEpsRequestDTO(
                "EPS001", "SERV_INEXISTENTE", new BigDecimal("70.00"));

        when(epsRepository.findById("EPS001")).thenReturn(Optional.of(eps));
        when(servicioRepository.findById("SERV_INEXISTENTE")).thenReturn(Optional.empty());

        // Act + Assert
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> tarifaEpsService.crearTarifa(request));

        assertTrue(exception.getMessage().contains("Servicio no encontrado"));
        verify(tarifaEpsRepository, never()).save(any(TarifaEps.class));
    }

    /**
     * Verifica que al intentar registrar una tarifa para una combinación
     * de EPS y servicio que ya existe, el sistema lance
     * {@link IllegalArgumentException} (regla de no duplicación).
     */
    @Test
    void crearTarifaDebeLanzarExcepcionSiYaExisteLaCombinacionEpsServicio() {
        // --- Patrón AAA ---

        // Arrange
        Eps eps = new Eps();
        eps.setCodigo("EPS001");

        Servicio servicio = new Servicio();
        servicio.setCodigo("SERV001");

        TarifaEpsRequestDTO request = new TarifaEpsRequestDTO(
                "EPS001", "SERV001", new BigDecimal("70.00"));

        when(epsRepository.findById("EPS001")).thenReturn(Optional.of(eps));
        when(servicioRepository.findById("SERV001")).thenReturn(Optional.of(servicio));
        when(tarifaEpsRepository.existsByEpsCodigoAndServicioCodigo("EPS001", "SERV001"))
                .thenReturn(true);

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> tarifaEpsService.crearTarifa(request));

        assertTrue(exception.getMessage().contains("Ya existe una tarifa"));
        verify(tarifaEpsRepository, never()).save(any(TarifaEps.class));
    }

    /**
     * Verifica que al crear una tarifa con datos válidos, el servicio
     * persista la entidad y retorne el DTO de respuesta correctamente.
     */
    @Test
    void crearTarifaDebeRetornarDtoCuandoLosDatosSonValidos() {
        // --- Patrón AAA ---

        // Arrange
        Eps eps = new Eps();
        eps.setCodigo("EPS001");
        eps.setNombre("Sura");

        Servicio servicio = new Servicio();
        servicio.setCodigo("SERV001");
        servicio.setNombre("Consulta General");

        BigDecimal porcentaje = new BigDecimal("70.00");
        TarifaEpsRequestDTO request = new TarifaEpsRequestDTO("EPS001", "SERV001", porcentaje);

        TarifaEps tarifaGuardada = TarifaEps.builder()
                .codigo("TAEPS001")
                .eps(eps)
                .servicio(servicio)
                .porcentajeCobertura(porcentaje)
                .estado(true)
                .build();

        TarifaEpsResponseDTO dtoEsperado = new TarifaEpsResponseDTO(
                "TAEPS001", "EPS001", "Sura", "SERV001", "Consulta General",
                porcentaje, true, LocalDateTime.now());

        when(epsRepository.findById("EPS001")).thenReturn(Optional.of(eps));
        when(servicioRepository.findById("SERV001")).thenReturn(Optional.of(servicio));
        when(tarifaEpsRepository.existsByEpsCodigoAndServicioCodigo("EPS001", "SERV001"))
                .thenReturn(false);
        when(tarifaEpsRepository.save(any(TarifaEps.class))).thenReturn(tarifaGuardada);
        when(tarifaEpsMapper.toResponseDTO(tarifaGuardada)).thenReturn(dtoEsperado);

        // Act
        TarifaEpsResponseDTO resultado = tarifaEpsService.crearTarifa(request);

        // Assert
        assertNotNull(resultado);
        assertEquals("TAEPS001", resultado.codigo());
        assertEquals("EPS001", resultado.codigoEps());
        assertEquals(porcentaje, resultado.porcentajeCobertura());
        verify(tarifaEpsRepository, times(1)).save(any(TarifaEps.class));
    }

    /**
     * Verifica que al consultar las tarifas activas de una EPS que no existe,
     * el sistema lance {@link RecursoNoEncontradoException}.
     */
    @Test
    void obtenerTarifasActivasPorEpsDebeLanzarExcepcionSiLaEpsNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        String codigoEpsInexistente = "EPS_FANTASMA";
        when(epsRepository.existsById(codigoEpsInexistente)).thenReturn(false);

        // Act + Assert
        RecursoNoEncontradoException exception = assertThrows(
                RecursoNoEncontradoException.class,
                () -> tarifaEpsService.obtenerTarifasActivasPorEps(codigoEpsInexistente));

        assertTrue(exception.getMessage().contains("EPS no encontrada"));
        verify(tarifaEpsRepository, never()).findByEpsCodigoAndEstadoTrue(anyString());
    }
}
