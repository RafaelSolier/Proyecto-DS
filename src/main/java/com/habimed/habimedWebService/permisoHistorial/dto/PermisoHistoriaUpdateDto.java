package com.habimed.habimedWebService.permisoHistorial.dto;

import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

@Data
public class PermisoHistoriaUpdateDto {
    @NotNull(message = "El id del doctor es obligatorio")
    private Integer iddoctor;
    @NotNull(message = "El id del paciente es obligatorio")
    private Integer idpaciente;
    @NotNull(message = "La fecha de inicio del permiso es obligatoria")
    private LocalDate fechaotorgapermiso;
    private LocalDate fechadeniegapermiso;
    private Boolean estado;
}
