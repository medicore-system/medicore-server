package com.medicore.api.services;

import com.medicore.api.entities.Usuario;

import java.util.List;
import java.util.Optional;
/**
 * Interfaz que define las operaciones de negocio
 * relacionadas con los usuarios del sistema.
 *
 * <p>Contiene los métodos necesarios para:
 * <ul>
 *     <li>Registrar usuarios</li>
 *     <li>Consultar usuarios</li>
 *     <li>Actualizar información de usuarios</li>
 *     <li>Inhabilitar usuarios</li>
 * </ul>
 * </p>
 *
 * @author Manuel
 */
public interface IUsuarioService {

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return lista de usuarios.
     */
    List<Usuario> findAll();

    /**
     * Busca un usuario por su documento.
     *
     * @param document documento del usuario.
     * @return Optional con el usuario encontrado
     * o vacío si no existe.
     */
    Optional<Usuario> findByDocumento(String document);

    /**
     * Guarda un usuario en el sistema.
     *
     * @param usuario entidad usuario a guardar.
     * @return usuario almacenado.
     */
    Usuario save(Usuario usuario);

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param documento documento del usuario a actualizar.
     * @param usuario entidad con la nueva información.
     * @return Optional con el usuario actualizado
     * o vacío si no existe.
     */
    Optional<Usuario> update(String documento, Usuario usuario);

    /**
     * Inhabilita un usuario del sistema.
     *
     * <p>El estado del usuario cambia a false.</p>
     *
     * @param document documento del usuario.
     * @return Optional con el usuario actualizado
     * o vacío si no existe.
     */
    Optional<Usuario> inhabilitarUsuario(String document);
}
