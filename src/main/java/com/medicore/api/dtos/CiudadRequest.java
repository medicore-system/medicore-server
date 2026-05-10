package com.medicore.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CiudadRequest {

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
    private String nombre;

    @NotNull(message = "El departamento es obligatorio")
    private Integer idDepartamento;
}
