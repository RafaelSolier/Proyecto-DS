package com.habimed.habimedWebService.cita.domain.service;

import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.consultorioServicioU.repository.ConsultorioServicioURepository;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.cita.domain.model.EstadoCitaEnum;
import com.habimed.habimedWebService.cita.dto.*;
import com.habimed.habimedWebService.cita.repository.CitaRepository;
import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final ModelMapper modelMapper;
    private final UsuarioRepository usuarioRepository;
    private final ConsultorioServicioURepository consultorioServicioURepository;

    @Override
    public List<CitaResponseDto> findAll() {
        List<Cita> citas = citaRepository.findAll();
        return citas.stream().map(c -> mapperCitaResponse(c)).collect(Collectors.toList());
    }

    @Override
    public List<CitaResponseDto> findAllWithConditions(CitaFilterDto citaFilterDto) {

        List<Cita> citas = citaRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (citaFilterDto.getIdCita() != null) {
            citas = citas.stream()
                    .filter(c -> c.getIdCita().equals(citaFilterDto.getIdCita()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getIdConsultorioServicioU() != null) {
             citas = citas.stream()
                     .filter(c -> c.getConsultorioServicioU() != null && c.getConsultorioServicioU().getIdConsultorioServicioU().equals(citaFilterDto.getIdConsultorioServicioU()))
                     .collect(Collectors.toList());
        }


        if (citaFilterDto.getIdPersona() != null) {
            citas = citas.stream()
                    .filter(c -> c.getPaciente() != null &&
                            c.getPaciente().getIdUsuario() != null &&
                            c.getPaciente().getIdUsuario().equals(citaFilterDto.getIdPersona()))
                    .collect(Collectors.toList());
        }

        if (citaFilterDto.getFechaHoraInicio() != null) {
            citas = citas.stream()
                    .filter(c -> c.getFechaHoraInicio() != null && 
                            c.getFechaHoraInicio().toLocalDate().equals(citaFilterDto.getFechaHoraInicio().toLocalDate()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getFechaHoraFin() != null) {
            citas = citas.stream()
                    .filter(c -> c.getFechaHoraFin() != null && 
                            c.getFechaHoraFin().toLocalDate().equals(citaFilterDto.getFechaHoraFin().toLocalDate()))
                    .collect(Collectors.toList());
        }
        
        if (citaFilterDto.getEstado() != null) {
            citas = citas.stream()
                    .filter(c -> c.getEstado() != null && c.getEstado().equals(citaFilterDto.getEstado()))
                    .collect(Collectors.toList());
        }
        
        return citas.stream().map(e -> mapperCitaResponse(e)).collect(Collectors.toList());
    }

    @Override
    public CitaResponseDto getById(Integer id) {
        Optional<Cita> cita = citaRepository.findById(id);
        if (cita.isPresent()) {
            return mapperCitaResponse(cita.get());
        }
        throw new RuntimeException("Cita no encontrada con ID: " + id);
    }

    @Override
    public CitaResponseDto save(CitaInsertDto citaInsertDto) {
        // Validaciones de negocio antes de guardar
        if (citaInsertDto.getFechaHoraInicio() != null && citaInsertDto.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("No se puede crear una cita con fecha pasada");
        }
        
        if (citaInsertDto.getFechaHoraInicio() != null && citaInsertDto.getFechaHoraFin() != null &&
            citaInsertDto.getFechaHoraFin().isBefore(citaInsertDto.getFechaHoraInicio())) {
            throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        
        Cita cita = modelMapper.map(citaInsertDto, Cita.class);
        Usuario paciente = usuarioRepository.findById(citaInsertDto.getIdPaciente()).orElseThrow(()->new RuntimeException("No existe el paciente"));
        cita.setPaciente(paciente);
        ConsultorioServicioU consul = consultorioServicioURepository.findById(citaInsertDto.getIdConsultorioServicioU()).orElseThrow(()->new RuntimeException("No existe la relación con el consultorio"));
        cita.setConsultorioServicioU(consul);


        // Establecer estado por defecto si no se proporciona
        if (cita.getEstado() == null) {
            cita.setEstado(EstadoCitaEnum.SOLICITADA);
        }
        
        Cita savedCita = citaRepository.save(cita);
        return mapperCitaResponse(savedCita);
    }

    @Override
    public CitaResponseDto update(Integer id, CitaUpdateDto citaUpdateDto) {
        Optional<Cita> existingCita = citaRepository.findById(id);
        
        if (existingCita.isPresent()) {
            Cita cita = existingCita.get();
            
            // Actualizar solo los campos que no son null en el DTO
            if (citaUpdateDto.getMotivo() != null && !citaUpdateDto.getMotivo().trim().isEmpty()) {
                cita.setMotivo(citaUpdateDto.getMotivo());
            }
            if (citaUpdateDto.getFechaHoraInicio() != null) {
                // Validar que no sea una fecha pasada
                if (citaUpdateDto.getFechaHoraInicio().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("No se puede actualizar a una fecha pasada");
                }
                cita.setFechaHoraInicio(citaUpdateDto.getFechaHoraInicio());
            }
            if (citaUpdateDto.getFechaHoraFin() != null) {
                cita.setFechaHoraFin(citaUpdateDto.getFechaHoraFin());
            }
            if (citaUpdateDto.getEstado() != null) {
                cita.setEstado(citaUpdateDto.getEstado());
            }
            
            // Validar consistencia de fechas después de las actualizaciones
            if (cita.getFechaHoraFin() != null && cita.getFechaHoraInicio() != null &&
                cita.getFechaHoraFin().isBefore(cita.getFechaHoraInicio())) {
                throw new RuntimeException("La fecha de fin no puede ser anterior a la fecha de inicio");
            }
            
            Cita updatedCita = citaRepository.save(cita);
            return mapperCitaResponse(updatedCita);
        }
        
        throw new RuntimeException("Cita no encontrada con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Cita> cita = citaRepository.findById(id);
        
        if (cita.isPresent()) {
            // Soft delete con estado CANCELADA para las citas eliminadas

            Cita citaEntity = cita.get();
            
            // Validar si la cita puede ser eliminada según su estado
            if (citaEntity.getEstado() == EstadoCitaEnum.REALIZADA || citaEntity.getEstado() == EstadoCitaEnum.CANCELADA) {
                throw new RuntimeException("No se puede eliminar una cita que ya fue completada o cancelada");
            }


//            // Verificar si tiene relaciones dependientes
//            if (citaEntity.getDiagnosticos() != null && !citaEntity.getDiagnosticos().isEmpty()) {
//                throw new RuntimeException("No se puede eliminar la cita porque tiene diagnósticos asociados");
//            }
//
//            if (citaEntity.getRecetas() != null && !citaEntity.getRecetas().isEmpty()) {
//                throw new RuntimeException("No se puede eliminar la cita porque tiene recetas asociadas");
//            }
//
//            if (citaEntity.getRecomendaciones() != null && !citaEntity.getRecomendaciones().isEmpty()) {
//                throw new RuntimeException("No se puede eliminar la cita porque tiene recomendaciones asociadas");
//            }
//
//            if (citaEntity.getDetallePago() != null) {
//                throw new RuntimeException("No se puede eliminar la cita porque tiene un pago asociado");
//            }
            citaEntity.setEstado(EstadoCitaEnum.CANCELADA);
            citaRepository.save(citaEntity);
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }

    private CitaResponseDto mapperCitaResponse(Cita cita){
        CitaResponseDto citaResponseDto = modelMapper.map(cita, CitaResponseDto.class);
        citaResponseDto.setIdPaciente(cita.getPaciente().getIdUsuario());
        citaResponseDto.setIdConsultorioServicioU(cita.getConsultorioServicioU().getIdConsultorioServicioU());
        return citaResponseDto;
    }
}