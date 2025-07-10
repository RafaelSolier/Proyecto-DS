package com.habimed.habimedWebService.permisoHistorial.dto;

import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

@Data
public class PermisoHistorialnsertDto {
    //private Integer idpermisohistorial;
    @NotNull(message = "El ID del doctor no puede ser nulo")
    private Integer iddoctor;
    @NotNull(message = "El ID del paciente no puede ser nulo")
    private Integer idpaciente;
    @NotNull(message = "La fecha de otorgamiento del permiso no puede ser nula")
    private LocalDate fechaotorgapermiso;
    private LocalDate fechadeniegapermiso;
    private Boolean estado;
}
