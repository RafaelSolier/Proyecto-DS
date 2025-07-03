package com.habimed.habimedWebService.especialidad.domain.service;

import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.servicio.domain.service.ServicioService;
import com.habimed.habimedWebService.servicio.dto.ServicioFilterDto;
import com.habimed.habimedWebService.servicio.dto.ServicioResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.dto.*;
import com.habimed.habimedWebService.especialidad.repository.EspecialidadRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EspecialidadServiceImpl implements EspecialidadService {

    private final EspecialidadRepository especialidadRepository;
    private final ModelMapper modelMapper;
    private final ServicioService servicioService;

    @Override
    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    @Override
    public List<Especialidad> findAllWithConditions(EspecialidadFilterDto especialidadFilterDto) {

        List<Especialidad> especialidades = especialidadRepository.findAll();

        if (especialidadFilterDto.getIdEspecialidad() != null) {
            especialidades = especialidades.stream()
                    .filter(e -> e.getIdEspecialidad().equals(especialidadFilterDto.getIdEspecialidad()))
                    .collect(Collectors.toList());
        }
        
        if (especialidadFilterDto.getNombre() != null && !especialidadFilterDto.getNombre().trim().isEmpty()) {
            especialidades = especialidades.stream()
                    .filter(e -> e.getNombre() != null && 
                            e.getNombre().toLowerCase().contains(especialidadFilterDto.getNombre().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        if (especialidadFilterDto.getDescripcionContiene() != null && 
            !especialidadFilterDto.getDescripcionContiene().trim().isEmpty()) {
            especialidades = especialidades.stream()
                    .filter(e -> e.getDescripcion() != null && 
                            e.getDescripcion().toLowerCase().contains(
                                    especialidadFilterDto.getDescripcionContiene().toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return especialidades;
    }

    @Override
    public EspecialidadResponseDto getById(Integer id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        if (especialidad.isPresent()) {
            return modelMapper.map(especialidad.get(), EspecialidadResponseDto.class);
        }
        throw new RuntimeException("Especialidad no encontrada con ID: " + id);
    }

    @Override
    public EspecialidadResponseDto save(EspecialidadInsertDto especialidadInsertDto) {

        // Verificar que no exista ya una especialidad con el mismo nombre (case insensitive)
        List<Especialidad> existingEspecialidades = especialidadRepository.findAll();
        boolean nombreExists = existingEspecialidades.stream()
                .anyMatch(e -> e.getNombre() != null && 
                        e.getNombre().equalsIgnoreCase(especialidadInsertDto.getNombre().trim()));
        
        if (nombreExists) {
            throw new RuntimeException("Ya existe una especialidad con el nombre: " + 
                    especialidadInsertDto.getNombre());
        }

        Especialidad especialidad = modelMapper.map(especialidadInsertDto, Especialidad.class);
        especialidad.setNombre(especialidadInsertDto.getNombre());
        
        // Normalizar descripción si existe
        if (especialidad.getDescripcion() != null && !especialidad.getDescripcion().trim().isEmpty()) {
            especialidad.setDescripcion(especialidad.getDescripcion().trim());
        }
        
        Especialidad savedEspecialidad = especialidadRepository.save(especialidad);
        return modelMapper.map(savedEspecialidad, EspecialidadResponseDto.class);
    }

    @Override
    public EspecialidadResponseDto update(Integer id, EspecialidadUpdateDto especialidadUpdateDto) {
        Optional<Especialidad> existingEspecialidad = especialidadRepository.findById(id);
        
        if (existingEspecialidad.isPresent()) {
            Especialidad especialidad = existingEspecialidad.get();
            
            // Validar nombre único si se está actualizando
            if (especialidadUpdateDto.getNombre() != null && 
                !especialidadUpdateDto.getNombre().trim().isEmpty() &&
                !especialidadUpdateDto.getNombre().equalsIgnoreCase(especialidad.getNombre())) {
                
                List<Especialidad> existingEspecialidades = especialidadRepository.findAll();
                boolean nombreExists = existingEspecialidades.stream()
                        .anyMatch(e -> e.getIdEspecialidad() != especialidad.getIdEspecialidad() &&
                                e.getNombre() != null && 
                                e.getNombre().equalsIgnoreCase(especialidadUpdateDto.getNombre().trim()));
                
                if (nombreExists) {
                    throw new RuntimeException("Ya existe una especialidad con el nombre: " + 
                            especialidadUpdateDto.getNombre());
                }
            }
            
            // Actualizar solo los campos que no son null en el DTO
            if (especialidadUpdateDto.getNombre() != null && !especialidadUpdateDto.getNombre().trim().isEmpty()) {
                especialidad.setNombre(especialidadUpdateDto.getNombre().trim());
            }
            
            if (especialidadUpdateDto.getDescripcion() != null) {
                if (especialidadUpdateDto.getDescripcion().trim().isEmpty()) {
                    especialidad.setDescripcion(null); // Permitir limpiar la descripción
                } else {
                    especialidad.setDescripcion(especialidadUpdateDto.getDescripcion().trim());
                }
            }
            
            Especialidad updatedEspecialidad = especialidadRepository.save(especialidad);
            return modelMapper.map(updatedEspecialidad, EspecialidadResponseDto.class);
        }
        
        throw new RuntimeException("Especialidad no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Especialidad> especialidad = especialidadRepository.findById(id);
        
        if (especialidad.isPresent()) {
            Especialidad especialidadEntity = especialidad.get();
            
            // Verificar si tiene servicios asociados antes de eliminar
            if (especialidadEntity.getServicios() != null && !especialidadEntity.getServicios().isEmpty()) {
                ServicioFilterDto servicioFilterDto = new ServicioFilterDto();
                servicioFilterDto.setIdEspecialidad(especialidadEntity.getIdEspecialidad());
                List<ServicioResponseDto> servicios = servicioService.findAllWithConditions(servicioFilterDto);
                if (!servicios.isEmpty()) {  // Verifica si la lista no está vacía
                    servicios.forEach(relacion -> {
                        servicioService.delete(relacion.getIdServicio());  // Ejecuta delete para cada ID
                    });
                }
            }
            
            especialidadRepository.deleteById(id);
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }


}