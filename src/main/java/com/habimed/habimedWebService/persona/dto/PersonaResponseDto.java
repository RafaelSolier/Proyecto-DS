package com.habimed.habimedWebService.persona.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonaResponseDto {
    private Integer id;
    private String dni;
    private String nombres;
    private String apellidos;
    private String celular;
    private String direccion;
    private LocalDate fechaNacimiento;
}
