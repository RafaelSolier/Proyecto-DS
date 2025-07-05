package com.habimed.habimedWebService.persona.domain.service;

import java.util.List;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.dto.*;

public interface PersonaService {
    List<PersonaResponseDto> findAll();
    List<PersonaResponseDto> findAllWithConditions(PersonaFilterDto personaFilterDto);
    Persona getById(Integer idPersona);
    PersonaResponseDto save(PersonaInsertDto personaInsertDto);
    Boolean delete(Integer idPersona);
    PersonaResponseDto update(Integer idPersona, PersonaUpdateDto personaUpdateDto);
}
