package com.medicore.api.exceptions.handler;

import com.medicore.api.dtos.ApiErrorResponse;
import com.medicore.api.exceptions.CiudadDuplicadaException;
import com.medicore.api.exceptions.RecursoNoEncontradoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.medicore.api.exceptions.ServicioDuplicadoException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidacion(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validación fallida")
                .mensaje("Uno o más campos son inválidos")
                .errores(errores)
                .build());
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Recurso no encontrado")
                .mensaje(ex.getMessage())
                .build());
    }

    @ExceptionHandler(CiudadDuplicadaException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicado(CiudadDuplicadaException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Ciudad duplicada")
                .mensaje(ex.getMessage())
                .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Recurso no encontrado")
                .mensaje(ex.getMessage())
                .build());
    }

    @ExceptionHandler(ServicioDuplicadoException.class)
    public ResponseEntity<ApiErrorResponse> handleServicioDuplicado(ServicioDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("Servicio duplicado")
                .mensaje(ex.getMessage())
                .build());
    }
}
