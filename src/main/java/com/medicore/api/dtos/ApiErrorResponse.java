package com.medicore.api.dtos;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ApiErrorResponse {

    private int status;
    private String error;
    private String mensaje;
    private List<String> errores;
}
