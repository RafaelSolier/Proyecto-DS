package com.habimed.habimedWebService.consultorio.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
public class ConsultorioInsertDto {
    @Pattern(regexp = "^\\d{11}$", message = "El RUC debe tener 11 dígitos")
    @NotBlank(message = "El RUC es obligatorio")
    private String ruc;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
    private String nombre;

    @NotBlank(message = "La latitud es obligatoria")
    @Size(max = 45, message = "La latitud no puede exceder 45 caracteres")
    private Double latitud;

    @NotBlank(message = "La longitud es obligatoria")
    @Size(max = 45, message = "La longitud no puede exceder 45 caracteres")
    private Double longitud;

    @Size(max = 45, message = "La dirección no puede exceder 45 caracteres")
    private String direccion;

    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;
}