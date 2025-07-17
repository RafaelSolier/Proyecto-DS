package com.habimed.habimedWebService.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String usuario;
    @NotBlank
    private String contrasenia;
}