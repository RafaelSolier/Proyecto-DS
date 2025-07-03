package com.habimed.habimedWebService.servicio.domain.service;

import java.util.List;

import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.servicio.dto.*;

public interface ServicioService {
    List<ServicioResponseDto> findAll();
    List<ServicioResponseDto> findAllWithConditions(ServicioFilterDto servicioFilterDto);
    Servicio getById(Integer id);
    ServicioResponseDto save(ServicioInsertDto servicioInsertDto);
    Boolean delete(Integer id);
    ServicioResponseDto update(Integer id, ServicioUpdateDto servicioUpdateDto);
}
