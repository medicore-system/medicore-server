package com.medicore.api.services.impl;

import com.medicore.api.entities.Usuario;
import com.medicore.api.repositories.IUsuarioRepository;
import com.medicore.api.services.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Implementación del servicio encargado de la lógica
 * de negocio relacionada con los usuarios.
 *
 * <p>Esta clase administra operaciones CRUD
 * y acciones relacionadas con el estado de los usuarios.</p>
 *
 * @author Manuel
 */
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {
    /**
     * Repositorio encargado de la persistencia
     * de usuarios.
     */
    private final IUsuarioRepository usuarioRepository;

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios.
     */
    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    /**
     * Busca un usuario por su documento.
     *
     * @param document documento del usuario.
     * @return Optional con el usuario encontrado
     * o vacío si no existe.
     */
    @Override
    public Optional<Usuario> findByDocumento(String document) {
        return usuarioRepository.findByDocumento(document);
    }

    /**
     * Guarda un usuario en la base de datos.
     *
     * @param usuario entidad usuario a guardar.
     * @return usuario almacenado.
     */
    @Override
    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    /**
     * Actualiza la información de un usuario existente.
     *
     * <p>Permite modificar:
     * <ul>
     *     <li>Nombre</li>
     *     <li>Apellido</li>
     *     <li>Correo</li>
     *     <li>EPS</li>
     *     <li>Ciudad</li>
     *     <li>Teléfono</li>
     * </ul>
     * </p>
     *
     * @param documento documento del usuario a actualizar.
     * @param usuario entidad con la nueva información.
     * @return Optional con el usuario actualizado
     * o vacío si no existe.
     */
    @Override
    public Optional<Usuario> update(String documento, Usuario usuario) {
        return usuarioRepository.findByDocumento(documento).map(existing ->{
           existing.setNombre(usuario.getNombre());
           existing.setApellido(usuario.getApellido());
           existing.setCorreo(usuario.getCorreo());
           existing.setEps(usuario.getEps());
           existing.setCiudad(usuario.getCiudad());
           existing.setTelefono(usuario.getTelefono());
           return usuarioRepository.save(existing);
        });
    }

    /**
     * Inhabilita un usuario del sistema.
     *
     * <p>El estado del usuario cambia a false.</p>
     *
     * @param document documento del usuario.
     * @return Optional con el usuario actualizado
     * o vacío si no existe.
     */
    @Override
    public Optional<Usuario> inhabilitarUsuario(String document) {
        return usuarioRepository.findByDocumento(document).map(existing ->{
            existing.setEstado(false);
            return usuarioRepository.save(existing);
        });
    }
}
