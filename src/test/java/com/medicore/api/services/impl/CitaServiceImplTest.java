package com.medicore.api.services.impl;

import com.medicore.api.entities.Cita.Cita;
import com.medicore.api.repositories.cita.ICitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link CitaServiceImpl}.
 *
 * <p>Verifica la lógica de cambio de estado de citas médicas
 * (aprobación y denegación) aplicando el patrón AAA
 * (Arrange, Act, Assert) y simulando el repositorio
 * con {@link Mock} de Mockito.</p>
 *
 * <p>Cada método de prueba se enfoca en una sola
 * responsabilidad para cumplir con el principio
 * de Single Responsibility de SOLID.</p>
 */
class CitaServiceImplTest {

    /**
     * Repositorio simulado de citas, evita el acceso real a la base de datos.
     */
    @Mock
    private ICitaRepository citaRepository;

    /**
     * Instancia del servicio bajo prueba, con los mocks inyectados.
     */
    @InjectMocks
    private CitaServiceImpl citaService;

    /**
     * Inicializa los mocks antes de cada prueba para garantizar
     * que cada test se ejecute de forma aislada e independiente.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifica que el método aprobar cambie el estado de la cita
     * a "APROBADA" cuando la cita existe en el sistema.
     */
    @Test
    void aprobarDebeCambiarEstadoAAprobada() {
        // --- Patrón AAA ---

        // Arrange
        String codigoCita = "CIT001";
        Cita citaExistente = new Cita();
        citaExistente.setCodigo(codigoCita);
        citaExistente.setEstado("PENDIENTE");

        when(citaRepository.findById(codigoCita)).thenReturn(Optional.of(citaExistente));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaExistente);

        // Act
        Optional<Cita> resultado = citaService.aprobar(codigoCita);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("APROBADA", resultado.get().getEstado());
    }

    /**
     * Verifica que el método aprobar retorne un Optional vacío
     * cuando se intenta aprobar una cita que no existe.
     */
    @Test
    void aprobarDebeRetornarVacioSiLaCitaNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        String codigoInexistente = "CIT999";
        when(citaRepository.findById(codigoInexistente)).thenReturn(Optional.empty());

        // Act
        Optional<Cita> resultado = citaService.aprobar(codigoInexistente);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(citaRepository, never()).save(any(Cita.class));
    }

    /**
     * Verifica que el método denegar cambie el estado de la cita
     * a "DENEGADA" cuando la cita existe en el sistema.
     */
    @Test
    void denegarDebeCambiarEstadoADenegada() {
        // --- Patrón AAA ---

        // Arrange
        String codigoCita = "CIT002";
        Cita citaExistente = new Cita();
        citaExistente.setCodigo(codigoCita);
        citaExistente.setEstado("PENDIENTE");

        when(citaRepository.findById(codigoCita)).thenReturn(Optional.of(citaExistente));
        when(citaRepository.save(any(Cita.class))).thenReturn(citaExistente);

        // Act
        Optional<Cita> resultado = citaService.denegar(codigoCita);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("DENEGADA", resultado.get().getEstado());
    }

    /**
     * Verifica que el método denegar retorne un Optional vacío
     * cuando se intenta denegar una cita que no existe.
     */
    @Test
    void denegarDebeRetornarVacioSiLaCitaNoExiste() {
        // --- Patrón AAA ---

        // Arrange
        String codigoInexistente = "CIT999";
        when(citaRepository.findById(codigoInexistente)).thenReturn(Optional.empty());

        // Act
        Optional<Cita> resultado = citaService.denegar(codigoInexistente);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(citaRepository, never()).save(any(Cita.class));
    }
}
