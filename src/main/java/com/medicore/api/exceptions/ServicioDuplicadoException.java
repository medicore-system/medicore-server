package com.medicore.api.exceptions;

/**
 * Excepción lanzada cuando se intenta crear un servicio médico con un nombre ya registrado.
 * Se mapea a un error HTTP 409 por el manejador global de excepciones.
 */
public class ServicioDuplicadoException extends RuntimeException {

    /**
     * Crea la excepción con el nombre del servicio duplicado.
     *
     * @param nombre nombre del servicio que ya existe en el sistema
     */
    public ServicioDuplicadoException(String nombre) {
        super("Ya existe un servicio registrado con el nombre: " + nombre);
    }
}