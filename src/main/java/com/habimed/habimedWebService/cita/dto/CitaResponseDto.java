package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaResponseDto {
    private Integer idCita;
    private Integer idPaciente;
    private Integer idConsultorioServicioU;
    private String motivo;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private EstadoCitaEnum estado;
    private String descripcion;
}
