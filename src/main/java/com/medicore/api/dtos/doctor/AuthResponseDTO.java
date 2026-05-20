package com.medicore.api.dtos.doctor;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class AuthResponseDTO{

    private String token;
    private String username;
    private List<String> roles;

}