package com.habimed.habimedWebService.servicio.domain.service;

import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.consultorioServicioU.domain.service.ConsultorioServicioUService;
import com.habimed.habimedWebService.consultorioServicioU.dto.FilterConsultorioServicioUDto;
import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import com.habimed.habimedWebService.especialidad.repository.EspecialidadRepository;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.servicio.dto.ServicioFilterDto;
import com.habimed.habimedWebService.servicio.dto.ServicioInsertDto;
import com.habimed.habimedWebService.servicio.dto.ServicioResponseDto;
import com.habimed.habimedWebService.servicio.dto.ServicioUpdateDto;
import com.habimed.habimedWebService.servicio.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServicioServiceImpl implements ServicioService {
    
    private final ServicioRepository servicioRepository;
    private final EspecialidadRepository especialidadRepository;
    private final ModelMapper modelMapper;
    private final ConsultorioServicioUService consultorioServicioUService;

    @Override
    public List<ServicioResponseDto> findAll() {
        List<Servicio> servicios = servicioRepository.findAll();

        modelMapper.typeMap(Servicio.class, ServicioResponseDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getEspecialidad().getIdEspecialidad(),
                            ServicioResponseDto::setIdEspecialidad);
                });
        return servicios.stream()
                .map(servicio -> modelMapper.map(servicio, ServicioResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ServicioResponseDto> findAllWithConditions(ServicioFilterDto servicioFilterDto) {

        List<Servicio> servicios = servicioRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (servicioFilterDto.getIdEspecialidad() != null) {
            servicios = servicios.stream()
                    .filter(s -> s.getEspecialidad() != null && 
                            s.getEspecialidad().getIdEspecialidad().equals(servicioFilterDto.getIdEspecialidad()))
                    .collect(Collectors.toList());
        }
        
        if (servicioFilterDto.getNombre() != null && !servicioFilterDto.getNombre().trim().isEmpty()) {
            String nombreBuscado = servicioFilterDto.getNombre().toLowerCase().trim();
            servicios = servicios.stream()
                    .filter(s -> s.getNombre() != null && 
                            s.getNombre().toLowerCase().contains(nombreBuscado))
                    .collect(Collectors.toList());
        }

        modelMapper.typeMap(Servicio.class, ServicioResponseDto.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getEspecialidad().getIdEspecialidad(),
                            ServicioResponseDto::setIdEspecialidad);
                });
        return servicios.stream()
                .map(servicio -> modelMapper.map(servicio, ServicioResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Servicio getById(Integer id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        if (servicio.isPresent()) {
            return servicio.get();
        }
        throw new RuntimeException("Servicio no encontrado con ID: " + id);
    }

    @Override
    public ServicioResponseDto save(ServicioInsertDto servicioInsertDto) {

        // Verificar que la especialidad existe
        Optional<Especialidad> especialidad = especialidadRepository.findById(servicioInsertDto.getIdEspecialidad());
        if (!especialidad.isPresent()) {
            throw new RuntimeException("No existe una especialidad con ID: " + servicioInsertDto.getIdEspecialidad());
        }
        
        Especialidad especialidadEntity = especialidad.get();
        
        Servicio servicio = modelMapper.map(servicioInsertDto, Servicio.class);
        servicio.setEspecialidad(especialidadEntity);
        servicio.setIdServicio(null); 
        Servicio savedServicio = servicioRepository.save(servicio);
        ServicioResponseDto responseDto = modelMapper.map(savedServicio, ServicioResponseDto.class);
        responseDto.setIdEspecialidad(savedServicio.getEspecialidad().getIdEspecialidad());
        return responseDto;
    }

    @Override
    public ServicioResponseDto update(Integer id, ServicioUpdateDto servicioUpdateDto) {
        Optional<Servicio> existingServicio = servicioRepository.findById(id);
        
        if (existingServicio.isPresent()) {
            Servicio servicio = existingServicio.get();
            
            // Actualizar solo los campos que no son null en el DTO
            if (servicioUpdateDto.getNombre() != null && !servicioUpdateDto.getNombre().trim().isEmpty()) {

                servicio.setNombre(servicioUpdateDto.getNombre());
            }
            
            if (servicioUpdateDto.getDescripcion() != null) {
                if (!servicioUpdateDto.getDescripcion().trim().isEmpty()) {
                    servicio.setDescripcion(servicioUpdateDto.getDescripcion());
                } else {
                    // Vaciar descripción si se envía cadena vacía
                    servicio.setDescripcion(null);
                }
            }
            
            if (servicioUpdateDto.getRiesgos() != null) {
                if (!servicioUpdateDto.getRiesgos().trim().isEmpty()) {
                    servicio.setRiesgos(servicioUpdateDto.getRiesgos());
                } else {
                    // Vaciar riesgos si se envía cadena vacía
                    servicio.setRiesgos(null);
                }
            }
            
            Servicio updatedServicio = servicioRepository.save(servicio);
            ServicioResponseDto responseDto = modelMapper.map(updatedServicio, ServicioResponseDto.class);
            responseDto.setIdEspecialidad(updatedServicio.getEspecialidad().getIdEspecialidad());
            return responseDto;
        }
        
        throw new RuntimeException("Servicio no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Servicio> servicio = servicioRepository.findById(id);
        
        if (servicio.isPresent()) {
            Servicio servicioEntity = servicio.get();

            FilterConsultorioServicioUDto dto = new FilterConsultorioServicioUDto();
            dto.setIdServicio(servicioEntity.getIdServicio());
            List<ConsultorioServicioU> relaciones = consultorioServicioUService.findAllWithConditions(dto);
            if (!relaciones.isEmpty()) {  // Verifica si la lista no está vacía
                relaciones.forEach(relacion -> {
                    consultorioServicioUService.delete(relacion.getIdConsultorioServicioU());
                });
            }
            servicioRepository.deleteById(id);
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }


}