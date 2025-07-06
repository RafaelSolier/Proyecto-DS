package com.habimed.habimedWebService.consultorioServicioU.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConsultorioServicioUResponseDto {
    private Integer idConsultorioServicioU;
    private Integer idUsuario;
    private Integer idConsultorio;
    private Integer idServicio;
}
