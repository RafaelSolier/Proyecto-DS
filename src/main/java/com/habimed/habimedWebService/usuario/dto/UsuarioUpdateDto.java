package com.habimed.habimedWebService.usuario.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;

@Data
public class UsuarioUpdateDto {

    private String codigoCMP;

    @Size(max = 255, message = "La contraseña no puede exceder 255 caracteres")
    private String contrasenia;

    private Boolean estado;
}