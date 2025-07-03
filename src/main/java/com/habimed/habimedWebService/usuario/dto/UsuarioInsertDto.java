package com.habimed.habimedWebService.usuario.dto;

import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UsuarioInsertDto {

    @NotNull(message = "El DI de la persona el obligatorio")
    private Integer idPersona;

    @Email
    @NotBlank
    private String correo;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private TipoUsuarioEnum tipoUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 255, message = "La contraseña no puede exceder 255 caracteres")
    private String contrasenia;

    private Boolean estado = false;

    private String codigoCMP;
}