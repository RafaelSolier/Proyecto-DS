package com.habimed.habimedWebService.permisoHistorial.dto;

import lombok.Data;

import java.time.LocalDate;

import com.habimed.habimedWebService.permisoHistorial.domain.model.EstadoPermisosEnum;

@Data
public class PermisoHistorialResponseDto {
    private Integer idPermisoHistorial;
    private Integer idDoctor;
    private Integer idPaciente;
    private LocalDate fechaOtorgaPermiso;
    private LocalDate fechaDeniegaPermiso;
    private EstadoPermisosEnum estado;
}
