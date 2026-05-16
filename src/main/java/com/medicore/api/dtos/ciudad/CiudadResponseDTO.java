package com.medicore.api.dtos.ciudad;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CiudadResponseDTO {

    private String code;
    private String name;
    private String department;
    private String status;
}
