package com.medicore.api.exceptions;

public class ServicioDuplicadoException extends RuntimeException {

    public ServicioDuplicadoException(String nombre) {
        super("Ya existe un servicio registrado con el nombre: " + nombre);
    }
}