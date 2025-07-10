package com.habimed.habimedWebService.permisoHistorial.dto;

import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

@Data
public class PermisoHistorialnsertDto {
    //private Integer idpermisohistorial;
    @NotNull(message = "El ID del doctor no puede ser nulo")
    private Integer idDoctor;
    @NotNull(message = "El ID del paciente no puede ser nulo")
    private Integer idPaciente;
    //private LocalDate fechaOtorgaPermiso;
    @NotNull(message = "La fecha de otorgamiento del permiso no puede ser nula")
    private LocalDate fechaDeniegaPermiso;
    private Boolean estado;
}
