package com.habimed.habimedWebService.consultorioServicioU.dto;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class FilterConsultorioServicioUDto {

    private Integer idUsuario;

    private Integer idConsultorio;

    private Integer idServicio;
}
