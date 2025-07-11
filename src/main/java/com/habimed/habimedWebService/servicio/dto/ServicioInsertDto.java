package com.habimed.habimedWebService.servicio.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ServicioInsertDto {
    @NotNull(message = "El ID de especialidad es obligatorio")
    private Integer idEspecialidad;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, min = 3, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 500, min = 3,message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 500, message = "Los riesgos no pueden exceder 500 caracteres")
    private String riesgos;
}