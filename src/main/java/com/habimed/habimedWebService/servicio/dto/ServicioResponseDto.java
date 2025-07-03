package com.habimed.habimedWebService.servicio.dto;

import com.habimed.habimedWebService.especialidad.dto.EspecialidadResponseDto;
import lombok.Data;

@Data
public class ServicioResponseDto {
    private Integer idServicio;
    private Integer idEspecialidad;
    private String nombre;
    private String descripcion;
    private String riesgos;
}