package com.medicore.api.dtos.ciudad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CiudadRequestDTO {

    @NotBlank(message = "El nombre de la ciudad es obligatorio")
    @Size(max = 50, message = "El nombre no debe superar los 50 caracteres")
    private String name;

    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 100, message = "El departamento no debe superar los 100 caracteres")
    private String department;

    @NotBlank(message = "El estado es obligatorio")
    private String status;
}
