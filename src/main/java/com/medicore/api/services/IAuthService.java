package com.medicore.api.services;

import com.medicore.api.dtos.AuthResponseDTO;
import com.medicore.api.dtos.LoginRequestDTO;
import com.medicore.api.dtos.RegisterRequestDTO;

public interface IAuthService {
    AuthResponseDTO register(RegisterRequestDTO request);

    AuthResponseDTO login(LoginRequestDTO request);
}
