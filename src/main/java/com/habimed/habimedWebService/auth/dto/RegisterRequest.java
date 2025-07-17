package com.habimed.habimedWebService.auth.dto;

import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 45, message = "Los nombres no pueden exceder 45 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 45, message = "Los apellidos no pueden exceder 45 caracteres")
    private String apellidos;

    @Pattern(regexp = "^\\d{9}$", message = "El celular debe tener 9 dígitos")
    private String celular;

    @Size(max = 45, message = "La dirección no puede exceder 45 caracteres")
    private String direccion;

    private LocalDate fechaNacimiento;

    @Email
    @NotBlank
    private String correo;

    @NotNull(message = "El tipo de usuario es obligatorio")
    private TipoUsuarioEnum tipoUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 255, message = "La contraseña no puede exceder 255 caracteres")
    private String contrasenia;

    private Boolean estado = true;

    private String codigoCMP;
}
