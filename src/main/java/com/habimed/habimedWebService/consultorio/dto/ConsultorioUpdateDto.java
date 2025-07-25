package com.habimed.habimedWebService.consultorio.dto;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
public class ConsultorioUpdateDto {
    @Pattern(regexp = "^\\d{11}$", message = "El RUC debe tener 11 dígitos")
    private String ruc;

    @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
    private String nombre;

    private Double longitud;

    private Double latitud;

    @Size(max = 45, message = "La dirección no puede exceder 45 caracteres")
    private String direccion;

    @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe tener 9 dígitos")
    private String telefono;
}