package com.medicore.api.controllers;

import com.medicore.api.dtos.cita.CitaSlotResponseDTO;
import com.medicore.api.entities.AsignacionMedico;
import com.medicore.api.repositories.IAsignacionMedicoRepository;
import com.medicore.api.repositories.cita.ICitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controlador REST que expone los datos necesarios para el
 * formulario de agendamiento de citas médicas.
 *
 * <p>El flujo esperado en el cliente es:
 * <ol>
 *   <li>El usuario selecciona tipo de cita y especialidad.</li>
 *   <li>Llama a {@code GET /disponibilidad/medicos?id_especialidad=X}
 *       para obtener los médicos disponibles.</li>
 *   <li>Selecciona un médico y llama a
 *       {@code GET /disponibilidad/fechas/{documentoMedico}}
 *       para ver los días disponibles.</li>
 *   <li>Selecciona una fecha y llama a
 *       {@code GET /disponibilidad/horarios/{documentoMedico}?fecha=YYYY-MM-DD}
 *       para ver los slots libres.</li>
 *   <li>Con médico + fecha + hora, hace POST a {@code /Citas}.</li>
 * </ol>
 * </p>
 *
 * Base URL: /disponibilidad
 */
@RestController
@RequestMapping("/disponibilidad")
@RequiredArgsConstructor
public class DisponibilidadCitaController {
    private final IAsignacionMedicoRepository asignacionRepo;
    private final ICitaRepository citaRepository;

    /** Costo por defecto. */
    private static final BigDecimal COSTO_DEFAULT = new BigDecimal("10000");

    /** Intervalo entre turnos en minutos. */
    private static final int INTERVALO_MIN = 30;

    /** Máximo de días hacia adelante que se generan. Evita listas enormes. */
    private static final int DIAS_HORIZONTE = 1;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Devuelve todos los slots disponibles para la especialidad y tipo dados.
     *
     * <p>Por cada asignación activa de médico con esa especialidad:
     * <ul>
     *   <li>Genera las fechas del rango (desde hoy, máximo {@value DIAS_HORIZONTE} días).</li>
     *   <li>Por cada fecha, genera los slots del turno del médico.</li>
     *   <li>Descarta los slots que ya tienen una cita registrada.</li>
     * </ul>
     * </p>
     *
     * @param idEspecialidad ID de la especialidad seleccionada por el usuario.
     * @param idTipo         ID del tipo de cita seleccionado por el usuario.
     */
    @GetMapping("/slots/{idEspecialidad}/{idTipo}")
    public ResponseEntity<List<CitaSlotResponseDTO>> slots(
            @PathVariable Integer idEspecialidad,
            @PathVariable Integer idTipo) {

        LocalDate hoy   = LocalDate.now();
        LocalDate limite = hoy.plusDays(DIAS_HORIZONTE);

        List<CitaSlotResponseDTO> result = asignacionRepo
                .findActivasByEspecialidad(idEspecialidad)
                .stream()
                .flatMap(asignacion -> generarSlotsDeAsignacion(asignacion, hoy, limite, idEspecialidad, idTipo))
                .toList();
        return ResponseEntity.ok(result);
    }

    private java.util.stream.Stream<CitaSlotResponseDTO> generarSlotsDeAsignacion(
            AsignacionMedico asignacion,
            LocalDate hoy,
            LocalDate limite,
            Integer idEspecialidad,
            Integer idTipo) {

        String documentoMedico = asignacion.getMedico().getDocumento();
        String turno           = asignacion.getHorario().getHorario(); // "07:00-13:00"

        // Fechas ya ocupadas por ese médico (para descartar rápido)
        Set<LocalDateTime> ocupados = citaRepository
                .findByMedicoDocumento(documentoMedico)
                .stream()
                .map(c -> c.getFecha()
                        .withSecond(0)
                        .withNano(0))
                .collect(Collectors.toSet());

        // Rango de la asignación recortado a hoy..límite
        LocalDate inicio = asignacion.getFechaInicio().isBefore(hoy) ? hoy : asignacion.getFechaInicio();
        LocalDate fin    = asignacion.getFechaFin().isAfter(limite)   ? limite : asignacion.getFechaFin();

        if (inicio.isAfter(fin)) return java.util.stream.Stream.empty();

        return inicio.datesUntil(fin.plusDays(1))
                .flatMap(fecha -> generarSlots(turno).stream()
                        .map(hora -> fecha.atTime(hora)
                                .withSecond(0)
                                .withNano(0))
                        .filter(dt -> !ocupados.contains(dt))
                        .map(dt -> toSlot(dt, asignacion, idEspecialidad, idTipo)));
    }

    private CitaSlotResponseDTO toSlot(LocalDateTime dt, AsignacionMedico a,
                               Integer idEspecialidad, Integer idTipo) {
        String fechaDisplay =
                dt.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                        + " " + dt.getDayOfMonth() + " de "
                        + dt.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                        + " - " + dt.format(TIME_FMT);

        return new CitaSlotResponseDTO(
                fechaDisplay,
                a.getMedico().getNombre() + " " + a.getMedico().getApellido(),
                a.getHospital().getNombre(),
                a.getCiudad().getNombre(),
                COSTO_DEFAULT,
                dt,
                a.getMedico().getDocumento(),
                a.getHospital().getCodigo(),
                idEspecialidad,
                idTipo
        );
    }

    private List<LocalTime> generarSlots(String turno) {
        try {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("h:mma", Locale.ENGLISH);

            String[] partes = turno.split("-");

            LocalTime cursor = LocalTime.parse(
                    partes[0].trim().toUpperCase(),
                    formatter
            );

            LocalTime fin = LocalTime.parse(
                    partes[1].trim().toUpperCase(),
                    formatter
            );

            List<LocalTime> slots = new ArrayList<>();

            while (cursor.isBefore(fin)) {
                slots.add(cursor);
                cursor = cursor.plusMinutes(INTERVALO_MIN);
            }

            return slots;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}