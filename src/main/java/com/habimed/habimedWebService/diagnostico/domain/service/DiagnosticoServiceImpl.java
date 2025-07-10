package com.habimed.habimedWebService.diagnostico.domain.service;

import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.diagnostico.dto.*;
import com.habimed.habimedWebService.diagnostico.repository.DiagnosticoRepository;
import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.cita.domain.model.Cita;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiagnosticoServiceImpl implements DiagnosticoService {
    
    private final DiagnosticoRepository diagnosticoRepository;
    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DiagnosticoResponseDto> findAll() {
        return diagnosticoRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public List<Diagnostico> findAllWithConditions(DiagnosticoFilterDto diagnosticoFilterDto) {
//        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
//        List<Diagnostico> diagnosticos = diagnosticoRepository.findAll();
//
//        // Filtrar por campos del FilterDto si no son null
//        if (diagnosticoFilterDto.getIdDiagnostico() != null) {
//            diagnosticos = diagnosticos.stream()
//                    .filter(d -> d.getIdDiagnostico().equals(diagnosticoFilterDto.getIdDiagnostico()))
//                    .collect(Collectors.toList());
//        }
//
//        if (diagnosticoFilterDto.getIdCita() != null) {
//            diagnosticos = diagnosticos.stream()
//                    .filter(d -> d.getCita() != null &&
//                            d.getCita().getIdCita().equals(diagnosticoFilterDto.getIdCita()))
//                    .collect(Collectors.toList());
//        }
//
//        if (diagnosticoFilterDto.getDescripcionContiene() != null &&
//            !diagnosticoFilterDto.getDescripcionContiene().trim().isEmpty()) {
//            diagnosticos = diagnosticos.stream()
//                    .filter(d -> d.getDescripcion() != null &&
//                            d.getDescripcion().toLowerCase().contains(
//                                    diagnosticoFilterDto.getDescripcionContiene().toLowerCase()))
//                    .collect(Collectors.toList());
//        }
//
//        if (diagnosticoFilterDto.getFechaDiagnosticoInicio() != null) {
//            diagnosticos = diagnosticos.stream()
//                    .filter(d -> d.getFechaDiagnostico() != null &&
//                            !d.getFechaDiagnostico().isBefore(diagnosticoFilterDto.getFechaDiagnosticoInicio()))
//                    .collect(Collectors.toList());
//        }
//
//        if (diagnosticoFilterDto.getFechaDiagnosticoFin() != null) {
//            diagnosticos = diagnosticos.stream()
//                    .filter(d -> d.getFechaDiagnostico() != null &&
//                            !d.getFechaDiagnostico().isAfter(diagnosticoFilterDto.getFechaDiagnosticoFin()))
//                    .collect(Collectors.toList());
//        }
//
//        return diagnosticos;
//    }

    @Override
    public DiagnosticoResponseDto getById(Integer id) {
        Optional<Diagnostico> diagnostico = diagnosticoRepository.findById(id);
        if (diagnostico.isPresent()) {
            return mapToResponseDto(diagnostico.get());
        }
        throw new RuntimeException("Diagnóstico no encontrado con ID: " + id);
    }

    @Override
    public DiagnosticoResponseDto save(DiagnosticoInsertDto diagnosticoInsertDto) {

        // Verificar que la cita existe
        Cita citaEntity = citaRepository.findById(diagnosticoInsertDto.getIdCita())
                .orElseThrow( () -> new ResourceNotFoundException("No existe una cita con ID: " + diagnosticoInsertDto.getIdCita()));
        
        // Verificar que la cita no sea futura (solo se pueden crear diagnósticos de citas pasadas o actuales)
        if (citaEntity.getFechaHoraInicio() != null && 
            citaEntity.getFechaHoraInicio().toLocalDate().isAfter(LocalDate.now())) {
            throw new ConflictException("No se puede crear un diagnóstico para una cita futura");
        }
        
        // Las Citas pueden tener más de un diagnostico de ser necesario
        // Por ejemplo por evaluaciones de diferentes doctores en una misma cita

        // Cargar los datos de entrada a un objeto nuevo y con la fecha actual
        Diagnostico diagnostico = modelMapper.map(diagnosticoInsertDto, Diagnostico.class);
        diagnostico.setCita(citaEntity);
        diagnostico.setFechaDiagnostico(LocalDate.now());

        diagnostico.setIdDiagnostico(null);

        // Guardar y mapear a respuesta
        Diagnostico savedDiagnostico = diagnosticoRepository.save(diagnostico);
        return mapToResponseDto(savedDiagnostico);
    }

    @Override
    public DiagnosticoResponseDto update(Integer id, DiagnosticoUpdateDto diagnosticoUpdateDto) {
        Optional<Diagnostico> existingDiagnostico = diagnosticoRepository.findById(id);
        
        if (existingDiagnostico.isPresent()) {
            Diagnostico diagnostico = existingDiagnostico.get();
            
//            // Verificar que el diagnóstico no sea muy antiguo (regla de negocio opcional)
//            if (diagnostico.getFechaDiagnostico() != null &&
//                diagnostico.getFechaDiagnostico().isBefore(LocalDate.now().minusDays(30))) {
//                throw new RuntimeException("No se puede modificar un diagnóstico con más de 30 días de antigüedad");
//            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (diagnosticoUpdateDto.getDescripcion() != null && 
                !diagnosticoUpdateDto.getDescripcion().trim().isEmpty()) {
                diagnostico.setDescripcion(diagnosticoUpdateDto.getDescripcion());
            }
            
            // Note: La fecha del diagnóstico y la cita asociada no se pueden cambiar una vez creado
            
            Diagnostico updatedDiagnostico = diagnosticoRepository.save(diagnostico);
            return mapToResponseDto(updatedDiagnostico);
        }
        
        throw new RuntimeException("Diagnóstico no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Diagnostico> diagnostico = diagnosticoRepository.findById(id);
        
        if (diagnostico.isPresent()) {
            Diagnostico diagnosticoEntity = diagnostico.get();
            
            // Verificar que el diagnóstico no sea muy antiguo (regla de negocio)
            if (diagnosticoEntity.getFechaDiagnostico() != null && 
                diagnosticoEntity.getFechaDiagnostico().isBefore(LocalDate.now().minusDays(7))) {
                throw new RuntimeException("No se puede eliminar un diagnóstico con más de 7 días de antigüedad");
            }
            
            // En un sistema real, podrías querer hacer un soft delete en lugar de eliminar físicamente
            diagnosticoRepository.deleteById(id);
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }


    private DiagnosticoResponseDto mapToResponseDto(Diagnostico diagnostico) {
        DiagnosticoResponseDto responseDto = modelMapper.map(diagnostico, DiagnosticoResponseDto.class);
        
        // Agregar información adicional de la cita si está disponible
        if (diagnostico.getCita() != null) {
            responseDto.setIdCita(diagnostico.getCita().getIdCita());
        }
        
        return responseDto;
    }
}