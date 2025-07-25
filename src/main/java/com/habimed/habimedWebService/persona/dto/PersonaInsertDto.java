package com.habimed.habimedWebService.persona.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class PersonaInsertDto {
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
}