package com.habimed.habimedWebService.consultorioServicioU.domain.service;

import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.consultorioServicioU.dto.ConsultorioServicioUInsertDto;
import com.habimed.habimedWebService.consultorioServicioU.dto.ConsultorioServicioUResponseDto;
import com.habimed.habimedWebService.consultorioServicioU.dto.FilterConsultorioServicioUDto;

import java.util.List;

public interface ConsultorioServicioUService {
    List<ConsultorioServicioUResponseDto> findAll();
    List<ConsultorioServicioUResponseDto> findAllWithConditions(FilterConsultorioServicioUDto filterConsultorioServicioUDto);
    ConsultorioServicioUResponseDto getById(Integer id);
    ConsultorioServicioU save(ConsultorioServicioUInsertDto consultorioServicioUInsertDto);
    Boolean delete(Integer id);
    ConsultorioServicioU update(Integer id, ConsultorioServicioUInsertDto consultorioServicioUUpdateDto);
}