package com.habimed.habimedWebService.consultorio.dto;

import lombok.Data;

@Data
public class ConsultorioDTO {
    private Integer idConsultorio;
    private String ruc;
    private String nombre;
    private Double latitud;
    private Double longitud;
    private String direccion;
    private String telefono;

}
