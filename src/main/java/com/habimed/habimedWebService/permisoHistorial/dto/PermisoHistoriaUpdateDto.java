package com.habimed.habimedWebService.permisoHistorial.dto;

import com.habimed.habimedWebService.permisoHistorial.domain.model.EstadoPermisosEnum;
import lombok.Data;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

@Data
public class PermisoHistoriaUpdateDto {
    //@NotNull(message = "El id del doctor es obligatorio")
    private Integer idDoctor;
    //@NotNull(message = "El id del paciente es obligatorio")
    private Integer idPaciente;
    private LocalDate fechaDeniegaPermiso;
    private EstadoPermisosEnum estado;
}
