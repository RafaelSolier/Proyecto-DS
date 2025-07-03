package com.habimed.habimedWebService.consultorio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorio.dto.*;

public interface ConsultorioService {

    // Agregar validaciones para Consultorios con el campo Estado

    // Retorna la informacion del consultorio sin los servicios que tiene
    List<ConsultorioResponseDto> findAll();

    // Retorna una lista con la informacion del consultorio sin los servicios que tiene
    List<ConsultorioResponseDto> findAllWithConditions(ConsultorioFilterDto consultorioFilterDto);

    ConsultorioResponseDto getById(Integer id);
    ConsultorioResponseDto save(ConsultorioInsertDto consultorioInsertDto);
    Boolean delete(Integer id);
    ConsultorioResponseDto update(Integer id, ConsultorioUpdateDto consultorioUpdateDto);
}
