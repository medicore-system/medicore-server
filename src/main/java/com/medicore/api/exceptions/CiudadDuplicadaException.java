package com.medicore.api.exceptions;

public class CiudadDuplicadaException extends RuntimeException {

    public CiudadDuplicadaException(String nombre, String departamento) {
        super("Ya existe una ciudad con el nombre '" + nombre + "' en el departamento '" + departamento + "'");
    }
}
