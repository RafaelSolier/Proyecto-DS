package com.habimed.habimedWebService.persona.domain.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.dto.*;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PersonaResponseDto> findAll() {
        return personaRepository.findAll().stream().map(persona -> modelMapper.map(persona, PersonaResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<PersonaResponseDto> findAllWithConditions(PersonaFilterDto personaFilterDto) {
        List<Persona> personas = personaRepository.findAll();

        if (personaFilterDto.getDni() != null && !personaFilterDto.getDni().trim().isEmpty()) {
            personas = personas.stream()
                    .filter(p -> p.getDni().equals(personaFilterDto.getDni()))
                    .collect(Collectors.toList());
        }

        if (personaFilterDto.getNombres() != null && !personaFilterDto.getNombres().trim().isEmpty()) {
            String nombresBuscados = personaFilterDto.getNombres().toLowerCase().trim();
            personas = personas.stream()
                    .filter(p -> p.getNombres() != null &&
                            p.getNombres().toLowerCase().contains(nombresBuscados))
                    .collect(Collectors.toList());
        }

        if (personaFilterDto.getApellidos() != null && !personaFilterDto.getApellidos().trim().isEmpty()) {
            String apellidosBuscados = personaFilterDto.getApellidos().toLowerCase().trim();
            personas = personas.stream()
                    .filter(p -> p.getApellidos() != null &&
                            p.getApellidos().toLowerCase().contains(apellidosBuscados))
                    .collect(Collectors.toList());
        }

        if (personaFilterDto.getFechaNacimiento() != null) {
            personas = personas.stream()
                    .filter(p -> p.getFechaNacimiento() != null &&
                            p.getFechaNacimiento().equals(personaFilterDto.getFechaNacimiento()))
                    .collect(Collectors.toList());
        }

        return personas.stream().map(persona -> modelMapper.map(persona, PersonaResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public PersonaResponseDto getById(Integer idPersona) {
        Optional<Persona> persona = personaRepository.findById(idPersona);
        if (persona.isPresent()) {
            return modelMapper.map(persona.get(), PersonaResponseDto.class);
        }
        throw new RuntimeException("Persona no encontrada con ID: " + idPersona);
    }

    @Override
    public PersonaResponseDto save(PersonaInsertDto personaInsertDto) {

        Persona persona = modelMapper.map(personaInsertDto, Persona.class);
        if (!personaRepository.findByDni(persona.getDni()).isEmpty()) {
            throw new RuntimeException("Persona ya existe");
        }
        persona.setId(null);
        Persona savedPersona = personaRepository.save(persona);
        return modelMapper.map(savedPersona, PersonaResponseDto.class);
    }

    @Override
    public PersonaResponseDto update(Integer idPersona, PersonaUpdateDto personaUpdateDto) {
        Optional<Persona> existingPersona = personaRepository.findById(idPersona);
        
        if (existingPersona.isPresent()) {
            Persona persona = existingPersona.get();
            
            // Actualizar solo los campos que no son null en el DTO
            if (personaUpdateDto.getNombres() != null && !personaUpdateDto.getNombres().trim().isEmpty()) {
                persona.setNombres(personaUpdateDto.getNombres());
            }
            
            if (personaUpdateDto.getApellidos() != null && !personaUpdateDto.getApellidos().trim().isEmpty()) {
                persona.setApellidos(personaUpdateDto.getApellidos());
            }

            if (personaUpdateDto.getCelular() != null && !personaUpdateDto.getCelular().trim().isEmpty()) {
                persona.setCelular(personaUpdateDto.getCelular());
            }
            
            if (personaUpdateDto.getDireccion() != null && !personaUpdateDto.getDireccion().trim().isEmpty()) {
                persona.setDireccion(personaUpdateDto.getDireccion());
            }
            
            if (personaUpdateDto.getFechaNacimiento() != null) {
                persona.setFechaNacimiento(personaUpdateDto.getFechaNacimiento());
            }
            
            Persona updatedPersona = personaRepository.save(persona);
            return modelMapper.map(updatedPersona, PersonaResponseDto.class);
        }
        
        throw new RuntimeException("Persona no encontrada con ID: " + idPersona);
    }

    @Override
    public Boolean delete(Integer idPersona) {
        if (personaRepository.existsById(idPersona)) {
            Optional<Persona> persona = personaRepository.findById(idPersona);
            
            if (persona.isPresent()) {
                Persona personaEntity = persona.get();
                
                // Verificar si tiene citas como paciente
                boolean tieneCitasComoPaciente = personaEntity.getUsuarios().stream()
                        .anyMatch(usuario -> usuario.getCitasComoPaciente() != null && 
                                !usuario.getCitasComoPaciente().isEmpty());
                
                if (tieneCitasComoPaciente) {
                    throw new RuntimeException("No se puede eliminar la persona porque tiene citas médicas asociadas como paciente");
                }

                
                personaRepository.deleteById(idPersona);
                return Boolean.TRUE;
            }
        }
        
        return Boolean.TRUE;
    }


}