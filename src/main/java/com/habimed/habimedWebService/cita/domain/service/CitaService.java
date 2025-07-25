package com.habimed.habimedWebService.cita.domain.service;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.*;

public interface CitaService {
    
    List<CitaResponseDto> findAll();

    List<CitaResponseDto> findAllWithConditions(CitaFilterDto citaFilterDto);

    CitaResponseDto getById(Integer id);

    CitaResponseDto save(CitaInsertDto citaInsertDto);

    Boolean delete(Integer id);

    CitaResponseDto update(Integer id, CitaUpdateDto citaUpdateDto);
}
