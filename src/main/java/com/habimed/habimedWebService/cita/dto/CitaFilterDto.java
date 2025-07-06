package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaFilterDto {
    private Integer idCita;

    private Integer idConsultorioServicioU;

    private Integer idPersona;

    private LocalDateTime fechaHoraInicio;

    private LocalDateTime fechaHoraFin;

    private EstadoCitaEnum estado;

}
