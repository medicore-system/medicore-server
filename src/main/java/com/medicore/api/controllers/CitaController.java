package com.medicore.api.controllers;

import com.medicore.api.dtos.Usuario.UsuarioResponseDTO;
import com.medicore.api.dtos.cita.CitaCreateRequestDTO;
import com.medicore.api.dtos.cita.CitaResponseDTO;
import com.medicore.api.entities.Cita;
import com.medicore.api.entities.Medico;
import com.medicore.api.entities.TipoCita;
import com.medicore.api.entities.Usuario;
import com.medicore.api.entities.hospital.Hospital;
import com.medicore.api.repositories.ICitaRepository;
import com.medicore.api.repositories.ITipoCitaRepository;
import com.medicore.api.repositories.IUsuarioRepository;
import com.medicore.api.repositories.MedicoRepository;
import com.medicore.api.repositories.hospital.HospitalRepository;
import com.medicore.api.services.ICitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Controlador REST encargado de gestionar las operaciones
 * relacionadas con las citas médicas.
 *
 * <p>Permite crear, consultar, aprobar y denegar citas.</p>
 *
 * Base URL: /Citas
 *
 * @author Manuel
 */
@RestController
@RequestMapping("/Citas")
@RequiredArgsConstructor
public class CitaController {
    /**
     * Servicio encargado de la lógica de negocio de las citas.
     */
    private final ICitaService citaService;
    /**
     * Repositorio para operaciones directas sobre citas.
     */
    private final ICitaRepository citaRepository;
    /**
     * Repositorio para la gestión de hospitales.
     */
    private final HospitalRepository hospitalRepository;
    /**
     * Repositorio para la gestión de usuarios.
     */
    private final IUsuarioRepository usuarioRepository;
    /**
     * Repositorio para la gestión de tipos de cita.
     */
    private final ITipoCitaRepository  tipoCitaRepository;
    /**
     * Repositorio para la gestión de medicos.
     */
    private final MedicoRepository medicoRepository;

    /**
     * Crea una nueva cita médica.
     *
     * <p>El método:
     * <ul>
     *     <li>Busca el hospital asociado.</li>
     *     <li>Busca el usuario/paciente asociado.</li>
     *     <li>Busca el tipo de cita.</li>
     *     <li>Genera automáticamente un código para la cita.</li>
     *     <li>Guarda la nueva cita en la base de datos.</li>
     * </ul>
     *
     * @param cita DTO con la información necesaria para crear la cita.
     * @return ResponseEntity con la cita creada y estado HTTP 201.
     */
    @PostMapping
    public ResponseEntity<CitaResponseDTO> save(@RequestBody CitaCreateRequestDTO cita){
        Hospital h = hospitalRepository.findById(cita.getCodigo_hospital())
                .orElseThrow(() -> new RuntimeException("Ciudad no encontrada"));
        Usuario u = usuarioRepository.findById(cita.getDocumento_paciente())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TipoCita tc = tipoCitaRepository.findById(cita.getId_tipo())
                .orElseThrow(() -> new RuntimeException("Tipo cita no encontrado"));
        Medico m = medicoRepository.findById(cita.getDocumento_medico())
                .orElseThrow(() -> new RuntimeException("Medico no encontrado"));
        Cita c = new Cita();
        int next = citaRepository.findMaxCodigoSquence() + 1;
        String codigo = String.format("CIT%03d", next);
        c.setCodigo(codigo);
        c.setFecha(cita.getFecha());
        c.setHora(cita.getHora());
        c.setCosto(cita.getCosto());
        c.setTipoCita(tc);
        c.setUsuario(u);
        c.setMedico(m);
        c.setHospital(h);

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(citaService.save(c)));
    }

    /**
     * Obtiene todas las citas registradas.
     *
     * @return lista de citas en formato DTO.
     */
    @GetMapping
    public ResponseEntity<List<CitaResponseDTO>> findAll(){
        List<CitaResponseDTO> response = citaService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Busca una cita por el documento del paciente.
     *
     * @param documento_paciente documento del paciente asociado a la cita.
     * @return la cita encontrada o 404 si no existe.
     */
    @GetMapping("/{documento_paciente}")
    public ResponseEntity<CitaResponseDTO> findByDocumento(@PathVariable String documento_paciente){
        return citaService.findByDocumento(documento_paciente)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    /**
     * Aprueba una cita médica.
     *
     * <p>El estado de la cita cambia a "APROBADA".</p>
     *
     * @param codigo código único de la cita.
     * @return la cita actualizada o 404 si no existe.
     */
    @PutMapping("/aprobar/{codigo}")
    public ResponseEntity<CitaResponseDTO> aprobar(@PathVariable String codigo){
        return citaService.findByCodigo(codigo)
                .map(existing -> {
                    existing.setEstado("APROBADA");
                    return toResponse(citaService.save(existing));
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deniega una cita médica.
     *
     * <p>El estado de la cita cambia a "DENEGADA".</p>
     *
     * @param codigo código único de la cita.
     * @return la cita actualizada o 404 si no existe.
     */
    @PutMapping("/denegar/{codigo}")
    public ResponseEntity<CitaResponseDTO> denegar(@PathVariable String codigo){
        return citaService.findByCodigo(codigo)
                .map(existing -> {
                    existing.setEstado("DENEGADA");
                    return toResponse(citaService.save(existing));
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Convierte una entidad {@link Cita} en un DTO de respuesta.
     *
     * <p>Este método encapsula la lógica de transformación
     * de entidades a objetos de transferencia de datos.</p>
     *
     * @param cita entidad cita.
     * @return DTO con la información de respuesta.
     */
    private CitaResponseDTO toResponse(Cita cita){
        CitaResponseDTO responseDTO = new CitaResponseDTO();
        responseDTO.setCodigo(cita.getCodigo());
        responseDTO.setEstado(cita.getEstado());
        responseDTO.setFecha(cita.getFecha());
        responseDTO.setHora(cita.getHora());
        responseDTO.setCosto(cita.getCosto());
        if(cita.getTipoCita()!=null){
            responseDTO.setTipoCita(cita.getTipoCita().getNombre());
        }
        if(cita.getUsuario()!=null){
            responseDTO.setNombreUsuario(cita.getUsuario().getNombre() + " " +  cita.getUsuario().getApellido());
        }
        if(cita.getHospital()!=null){
            responseDTO.setHospital(cita.getHospital().getNombre());
        }
        if(cita.getMedico()!=null){
            responseDTO.setMedico(cita.getMedico().getNombre() + " " +  cita.getMedico().getApellido());
        }
        return responseDTO;
    }


}
