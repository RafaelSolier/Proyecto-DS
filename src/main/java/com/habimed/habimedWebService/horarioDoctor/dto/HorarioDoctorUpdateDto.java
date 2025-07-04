package com.habimed.habimedWebService.horarioDoctor.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
public class HorarioDoctorUpdateDto {
    @Size(max = 15, message = "El día de la semana no puede exceder 15 caracteres")
    private String diaSemana;
    @NotNull(message = "El horario de inicio no puede ser nulo")
    private LocalDateTime horaInicio;
    @NotNull(message = "El horario de finalización no puede ser nulo")
    private LocalDateTime horaFin;
}