package com.habimed.habimedWebService.cita.dto;

import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CitaInsertDto {
    @NotNull
    private Integer idPaciente;
    @NotNull
    private Integer idConsultorioServicioU;
    @NotBlank
    private String motivo;

    @NotBlank
    private LocalDateTime fechaHoraInicio;
    @NotBlank
    private LocalDateTime fechaHoraFin;
    @NotBlank
    private EstadoCitaEnum estado;
    @NotBlank
    private String descripcion;
}
