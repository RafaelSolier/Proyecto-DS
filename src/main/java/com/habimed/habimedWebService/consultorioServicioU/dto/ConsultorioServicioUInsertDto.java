package com.habimed.habimedWebService.consultorioServicioU.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@RequiredArgsConstructor
public class ConsultorioServicioUInsertDto {

    @NotNull(message = "El id del Usuario Doctor es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El id del Consultorio es obligatorio")
    private Integer idConsultorio;

    @NotNull(message = "El id del Servicio es obligatorio")
    private Integer idServicio;
}
