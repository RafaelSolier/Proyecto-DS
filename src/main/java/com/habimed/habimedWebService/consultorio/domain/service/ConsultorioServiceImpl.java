package com.habimed.habimedWebService.consultorio.domain.service;

import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorio.dto.*;
import com.habimed.habimedWebService.consultorio.repository.ConsultorioRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultorioServiceImpl implements ConsultorioService {

    private final ConsultorioRepository consultorioRepository;
    private final ModelMapper modelMapper;
    private static final double COORDINATE_TOLERANCE = 0.000002;

    @Override
    public List<ConsultorioResponseDto> findAll() {
        List<Consultorio> consultorios = consultorioRepository.findByEstadoTrue();
        return consultorios.stream()
                .map(consultorio -> modelMapper.map(consultorio, ConsultorioResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultorioResponseDto> findAllWithConditions(ConsultorioFilterDto consultorioFilterDto) {
        List<Consultorio> consultorios = consultorioRepository.findAll();

        // Filtrar por campos del FilterDto si no son null
        if (consultorioFilterDto.getNombre() != null && !consultorioFilterDto.getNombre().trim().isEmpty()) {
            consultorios = consultorios.stream()
                    .filter(c -> c.getNombre() != null &&
                            c.getNombre().toLowerCase().contains(consultorioFilterDto.getNombre().toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtro por latitud
        if (consultorioFilterDto.getLatitud() != null) {
            consultorios = consultorios.stream()
                    .filter(c -> c.getLatitud() != null &&
                            c.getLatitud().equals(consultorioFilterDto.getLatitud()))
                    .collect(Collectors.toList());
        }

        // Filtro por longitud
        if (consultorioFilterDto.getLongitud() != null) {
            consultorios = consultorios.stream()
                    .filter(c -> c.getLongitud() != null &&
                            c.getLongitud().equals(consultorioFilterDto.getLongitud()))
                    .collect(Collectors.toList());
        }

        if (consultorioFilterDto.getRuc() != null && !consultorioFilterDto.getRuc().trim().isEmpty()) {
            consultorios = consultorios.stream()
                    .filter(c -> c.getRuc() != null &&
                            c.getRuc().equals(consultorioFilterDto.getRuc()))
                    .collect(Collectors.toList());
        }

        // Convertir a DTO usando ModelMapper
        return consultorios.stream()
                .map(consultorio -> modelMapper.map(consultorio, ConsultorioResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ConsultorioResponseDto getById(Integer id) {
        Optional<Consultorio> consultorio = consultorioRepository.findById(id);
        if (consultorio.isPresent()) {
            if (consultorio.get().getEstado()) {
                return modelMapper.map(consultorio.get(), ConsultorioResponseDto.class);
            }
            throw new ResourceNotFoundException("Consultorio eliminado");
        }
        throw new RuntimeException("Consultorio no encontrado con ID: " + id);
    }

    @Override
    public ConsultorioResponseDto save(ConsultorioInsertDto consultorioInsertDto) {
        // Validaciones específicas del contexto de Consultorio

        // Verificar si ya existe un consultorio con ese RUC
        List<Consultorio> existingConsultorios = consultorioRepository.findAll();
        boolean rucExists = existingConsultorios.stream()
                .anyMatch(c -> c.getRuc() != null && c.getRuc().equals(consultorioInsertDto.getRuc()));

        if (rucExists) {
            throw new ConflictException("Ya existe un consultorio con RUC: " + consultorioInsertDto.getRuc());
        }

        
        // Verificar si ya existe un consultorio con el mismo nombre en la misma ubicación
        boolean duplicateExists = existingConsultorios.stream()
                .anyMatch(c ->
                        c.getNombre() != null &&
                                c.getLatitud() != null &&
                                c.getLongitud() != null &&
                                c.getNombre().equalsIgnoreCase(consultorioInsertDto.getNombre()) &&
                                Math.abs(c.getLatitud() - consultorioInsertDto.getLatitud()) < COORDINATE_TOLERANCE &&
                                Math.abs(c.getLongitud() - consultorioInsertDto.getLongitud()) < COORDINATE_TOLERANCE
                );
        
        if (duplicateExists) {
            throw new ConflictException("Ya existe un consultorio con el nombre '" +
                    consultorioInsertDto.getNombre() + "' en la ubicación: " +
                    consultorioInsertDto.getLatitud() + ", "+ consultorioInsertDto.getLongitud());
        }
        
        Consultorio consultorio = modelMapper.map(consultorioInsertDto, Consultorio.class);
        consultorio.setIdConsultorio(null);
        Consultorio savedConsultorio = consultorioRepository.save(consultorio);
        return modelMapper.map(savedConsultorio, ConsultorioResponseDto.class);
    }

    @Override
    public ConsultorioResponseDto update(Integer id, ConsultorioUpdateDto consultorioUpdateDto) {
        Optional<Consultorio> existingConsultorio = consultorioRepository.findById(id);
        
        if (existingConsultorio.isPresent()) {
            Consultorio consultorio = existingConsultorio.get();
            
            // Validar RUC único si se está actualizando
            if (consultorioUpdateDto.getRuc() != null &&
                    !consultorioUpdateDto.getRuc().isBlank() &&
                    !consultorioUpdateDto.getRuc().equals(consultorio.getRuc())) {
                
                List<Consultorio> existingConsultorios = consultorioRepository.findAll();
                boolean rucExists = existingConsultorios.stream()
                        .anyMatch(c -> c.getIdConsultorio() != consultorio.getIdConsultorio() &&
                                c.getRuc() != null && c.getRuc().equals(consultorioUpdateDto.getRuc()));
                
                if (rucExists) {
                    throw new RuntimeException("Ya existe un consultorio con RUC: " + consultorioUpdateDto.getRuc());
                }
            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (consultorioUpdateDto.getRuc() != null && !consultorioUpdateDto.getRuc().trim().isEmpty()) {
                consultorio.setRuc(consultorioUpdateDto.getRuc());
            }
            if (consultorioUpdateDto.getNombre() != null && !consultorioUpdateDto.getNombre().trim().isEmpty()) {
                consultorio.setNombre(consultorioUpdateDto.getNombre());
            }
            if (consultorioUpdateDto.getLatitud() != null) {
                consultorio.setLatitud(consultorioUpdateDto.getLatitud());
            }

            if (consultorioUpdateDto.getLongitud() != null) {
                consultorio.setLongitud(consultorioUpdateDto.getLongitud());
            }
            if (consultorioUpdateDto.getDireccion() != null && !consultorioUpdateDto.getDireccion().trim().isEmpty()) {
                consultorio.setDireccion(consultorioUpdateDto.getDireccion());
            }
            if (consultorioUpdateDto.getTelefono() != null && !consultorioUpdateDto.getTelefono().trim().isEmpty()) {
                consultorio.setTelefono(consultorioUpdateDto.getTelefono());
            }
            
            Consultorio updatedConsultorio = consultorioRepository.save(consultorio);
            return modelMapper.map(updatedConsultorio, ConsultorioResponseDto.class);
        }
        
        throw new RuntimeException("Consultorio no encontrado con ID: " + id);
    }

    @Transactional
    @Override
    public Boolean delete(Integer id) {
        Consultorio consultorio = consultorioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Consultorio no encontrado"));
        consultorio.setEstado(Boolean.FALSE); // Eliminación lógica
        consultorioRepository.save(consultorio);
        return Boolean.TRUE;
    }

}