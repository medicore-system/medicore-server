package com.medicore.api.dtos.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicoResponseDTO {
    private String document;
    private String name;
    private String lastName;
    private String specialty;
    private String phone;
    private String email;
    private String status;

}