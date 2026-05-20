package com.medicore.api.exceptions;

/**
 * Excepción lanzada cuando se intenta crear una ciudad que ya existe en el mismo departamento.
 */
public class CiudadDuplicadaException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje que incluye el nombre de la ciudad y el departamento.
     *
     * @param nombre       nombre de la ciudad duplicada
     * @param departamento nombre del departamento donde ya existe
     */
    public CiudadDuplicadaException(String nombre, String departamento) {
        super("Ya existe una ciudad con el nombre '" + nombre + "' en el departamento '" + departamento + "'");
    }
}
