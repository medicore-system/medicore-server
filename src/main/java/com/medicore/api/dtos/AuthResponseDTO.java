package com.medicore.api.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String correo;
    private String role;
    private String documento;
}
