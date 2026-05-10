package com.medicore.api.dtos.doctor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//Request --> Lo que el frontend envia(la info que enviamos)
public class MedicoRequestDTO {
    private String document;
    private String name;
    private String lastName;
    private String specialty;
    private String phone;
    private String email;

}