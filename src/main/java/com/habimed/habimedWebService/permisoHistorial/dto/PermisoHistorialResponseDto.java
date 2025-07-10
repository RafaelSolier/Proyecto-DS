package com.habimed.habimedWebService.permisoHistorial.dto;

import lombok.Data;

import java.time.LocalDate;

import com.habimed.habimedWebService.permisoHistorial.domain.model.EstadoPermisosEnum;

@Data
public class PermisoHistorialResponseDto {
    private Integer idpermisohistorial;
    private Integer iddoctor;
    private Integer idpaciente;
    private LocalDate fechaotorgapermiso;
    private LocalDate fechadeniegapermiso;
    private EstadoPermisosEnum estado;
}
