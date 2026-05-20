package com.medicore.api.exceptions;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en la base de datos.
 * Se mapea a un error HTTP 404 por el manejador global de excepciones.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    /**
     * Crea la excepción con el mensaje descriptivo del recurso no encontrado.
     *
     * @param mensaje descripción del recurso que no fue encontrado
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
