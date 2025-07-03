package com.habimed.habimedWebService.consultorio.dto;

import lombok.Data;

@Data
public class ConsultorioFilterDto {
    private String nombre;
    private Double latitud;
    private Double longitud;
    private String ruc;
}
