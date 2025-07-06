package com.habimed.habimedWebService.persona.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonaFilterDto {
    private String dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
}