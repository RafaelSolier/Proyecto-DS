package com.habimed.habimedWebService.permisoHistorial.domain.service;

import com.habimed.habimedWebService.exception.BadRequestException;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import com.habimed.habimedWebService.permisoHistorial.domain.model.EstadoPermisosEnum;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistoriaUpdateDto;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialFilterDto;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialResponseDto;
import com.habimed.habimedWebService.permisoHistorial.dto.PermisoHistorialnsertDto;
import com.habimed.habimedWebService.permisoHistorial.repository.PermisoHistorialRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.habimed.habimedWebService.permisoHistorial.domain.model.PermisosHistorial;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class PermisoHistorialServiceImpl implements PermisoHistorialService {
    
    private final PermisoHistorialRepository permisoHistorialRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PermisosHistorial> findAll() {
        List<PermisosHistorial> lista = permisoHistorialRepository.findAll();
        return lista.stream()
                .map(permiso -> modelMapper.map(permiso, PermisosHistorial.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PermisosHistorial> findAllWithConditions(PermisoHistorialFilterDto permisoHistorialFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<PermisosHistorial> permisos = permisoHistorialRepository.findAll();
        
        // Filtrar por campos del FilterDto si no son null
        if (permisoHistorialFilterDto.getIdpermisohistorial() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getIdPermisoHistorial().equals(permisoHistorialFilterDto.getIdpermisohistorial()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getIddoctor() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getDoctor() != null && 
                            p.getDoctor().getIdUsuario().equals(permisoHistorialFilterDto.getIddoctor()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getIdpaciente() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getPaciente() != null && 
                            p.getPaciente().getIdUsuario().equals(permisoHistorialFilterDto.getIdpaciente()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getFechaotorgapermiso() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getFechaOtorgaPermiso() != null && 
                            p.getFechaOtorgaPermiso().equals(permisoHistorialFilterDto.getFechaotorgapermiso()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getFechadeniegapermiso() != null) {
            permisos = permisos.stream()
                    .filter(p -> p.getFechaDeniegaPermiso() != null &&
                            p.getFechaDeniegaPermiso().equals(permisoHistorialFilterDto.getFechadeniegapermiso()))
                    .collect(Collectors.toList());
        }
        
        if (permisoHistorialFilterDto.getEstado() != null) {
            EstadoPermisosEnum estadoBuscado = permisoHistorialFilterDto.getEstado() ?
                    EstadoPermisosEnum.ACTIVO : EstadoPermisosEnum.INACTIVO;
            permisos = permisos.stream()
                    .filter(p -> p.getEstado() != null && p.getEstado().equals(estadoBuscado))
                    .collect(Collectors.toList());
        }
        
        return permisos;
    }

    @Override
    public PermisoHistorialResponseDto getById(Integer id) {
        Optional<PermisosHistorial> permisoOpt = permisoHistorialRepository.findById(id);

        if (permisoOpt.isPresent()) {
            PermisosHistorial permiso = permisoOpt.get();

            return toDto(permiso);
        }
        throw new ResourceNotFoundException("Permiso no encontrado");
    }

    @Override
    public PermisoHistorialResponseDto save(PermisoHistorialnsertDto permisoHistorialInsertDto) {
        // Verificar que el doctor y paciente existen
        Optional<Usuario> doctor = usuarioRepository.findById(permisoHistorialInsertDto.getIdDoctor());
        if (!doctor.isPresent()) {
            throw new ResourceNotFoundException("No existe un doctor con ID: " + permisoHistorialInsertDto.getIdDoctor());
        }
        
        Optional<Usuario> paciente = usuarioRepository.findById(permisoHistorialInsertDto.getIdPaciente());
        if (!paciente.isPresent()) {
            throw new ResourceNotFoundException("No existe un paciente con ID: " + permisoHistorialInsertDto.getIdPaciente());
        }
        
        // Verificar que el doctor sea realmente un doctor y el paciente sea realmente un paciente
        if (doctor.get().getTipoUsuario() != TipoUsuarioEnum.DOCTOR) {
            throw new BadRequestException("El usuario con ID " + permisoHistorialInsertDto.getIdDoctor() + " no es un doctor");
        }
        
        if (paciente.get().getTipoUsuario() != TipoUsuarioEnum.PACIENTE) {
            throw new BadRequestException("El usuario con ID " + permisoHistorialInsertDto.getIdPaciente() + " no es un paciente");
        }
        
        // Verificar que un paciente no se otorgue permisos a sí mismo
        if (permisoHistorialInsertDto.getIdDoctor().equals(permisoHistorialInsertDto.getIdPaciente())) {
            throw new ConflictException("Un usuario no puede otorgarse permisos de historial a sí mismo");
        }
        
        // Verificar si ya existe un permiso activo entre este doctor y paciente
        List<PermisosHistorial> permisosExistentes = permisoHistorialRepository.findAll();
        boolean permisoActivoExists = permisosExistentes.stream()
               .anyMatch(p -> p.getDoctor() != null && p.getPaciente() != null &&
                       p.getDoctor().getIdUsuario().equals(permisoHistorialInsertDto.getIdDoctor()) &&
                       p.getPaciente().getIdUsuario().equals(permisoHistorialInsertDto.getIdPaciente()) &&
                       p.getEstado() == EstadoPermisosEnum.ACTIVO &&
                       p.getFechaDeniegaPermiso() == null);
        
        if (permisoActivoExists) {
            throw new ConflictException("Ya existe un permiso activo entre el doctor ID " + 
                    permisoHistorialInsertDto.getIdDoctor() + " y el paciente ID " +
                    permisoHistorialInsertDto.getIdPaciente());
        }
        
        PermisosHistorial permiso = modelMapper.map(permisoHistorialInsertDto, PermisosHistorial.class);
        permiso.setDoctor(doctor.get());
        permiso.setPaciente(paciente.get());
        
        // Establecer fecha de otorgamiento si no se proporciona
        if (permiso.getFechaOtorgaPermiso() == null) {
            permiso.setFechaOtorgaPermiso(LocalDate.now());
        }
        
        // Establecer estado por defecto como ACTIVO
        if (permiso.getEstado() == null) {
            permiso.setEstado(EstadoPermisosEnum.ACTIVO);
        }

        // Validar que la fecha de revocación del permiso no sea menor a la fecha de otorgamiento del permiso
        if (permisoHistorialInsertDto.getFechaDeniegaPermiso().isBefore(permiso.getFechaOtorgaPermiso())) {
            throw new BadRequestException("La fecha de cuando se deberá revocar el permiso debe ser mayor a la fecha de otorgamiento del permiso");
        }

        permiso.setIdPermisoHistorial(null);
        PermisosHistorial savedPermiso = permisoHistorialRepository.save(permiso);
        return modelMapper.map(savedPermiso, PermisoHistorialResponseDto.class);
    }

    @Override
    public PermisoHistorialResponseDto update(Integer id, PermisoHistoriaUpdateDto permisoHistoriaUpdateDto) {
        Optional<PermisosHistorial> existingPermiso = permisoHistorialRepository.findById(id);
        
        if (existingPermiso.isPresent()) {
            PermisosHistorial permiso = existingPermiso.get();
            
            // Validar que el permiso no esté ya denegado
            if (permiso.getEstado() == EstadoPermisosEnum.INACTIVO && permiso.getFechaDeniegaPermiso() != null) {
                throw new BadRequestException("No se puede modificar un permiso que ya ha sido denegado");
            }
            
            // Solo se puede actualizar el estado para denegar el permiso
            if (permisoHistoriaUpdateDto.getEstado() != null) {
                EstadoPermisosEnum nuevoEstado = permisoHistoriaUpdateDto.getEstado();
                
                // Si se está denegando el permiso, establecer fecha de denegación
                if (nuevoEstado == EstadoPermisosEnum.INACTIVO && permiso.getEstado() == EstadoPermisosEnum.ACTIVO) {
                    permiso.setFechaDeniegaPermiso(LocalDate.now());
                }
                
                // Si se está reactivando un permiso, limpiar fecha de denegación
                if (nuevoEstado == EstadoPermisosEnum.ACTIVO && permiso.getEstado() == EstadoPermisosEnum.INACTIVO) {
                    permiso.setFechaDeniegaPermiso(null);
                }
                
                permiso.setEstado(nuevoEstado);
            }

            // No permitir cambios en doctor, paciente o fecha de otorgamiento una vez creado
            if (permisoHistoriaUpdateDto.getFechaDeniegaPermiso() != null) {
                // Solo actualizar si se está denegando manualmente
                if (permiso.getEstado() == EstadoPermisosEnum.INACTIVO) {
                    if (permisoHistoriaUpdateDto.getFechaDeniegaPermiso().isBefore(permiso.getFechaOtorgaPermiso())) {
                        throw new BadRequestException("La fecha de denegación no puede ser anterior a la fecha de otorgamiento");
                    }
                    permiso.setFechaDeniegaPermiso(permisoHistoriaUpdateDto.getFechaDeniegaPermiso());
                }
            }
            
            PermisosHistorial updatedPermiso = permisoHistorialRepository.save(permiso);

            return toDto(updatedPermiso);
        }
        
        throw new ResourceNotFoundException("Permiso de historial no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<PermisosHistorial> permiso = permisoHistorialRepository.findById(id);
        
        if (permiso.isPresent()) {
            PermisosHistorial permisoEntity = permiso.get();
            
            // En lugar de eliminar físicamente, marcar como inactivo (soft delete)
            if (permisoEntity.getEstado() == EstadoPermisosEnum.ACTIVO) {
                permisoEntity.setEstado(EstadoPermisosEnum.INACTIVO);
                permisoEntity.setFechaDeniegaPermiso(LocalDate.now());
                permisoHistorialRepository.save(permisoEntity);
                return true;
            }
            permisoHistorialRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public PermisoHistorialResponseDto toDto(PermisosHistorial permisoHistorial) {
        PermisoHistorialResponseDto responseDto = modelMapper.map(permisoHistorial, PermisoHistorialResponseDto.class);
        responseDto.setIdDoctor(permisoHistorial.getDoctor().getIdUsuario());
        responseDto.setIdPaciente(permisoHistorial.getPaciente().getIdUsuario());
        return responseDto;
    }

    // Métodos helper específicos para el contexto de permisos
    public boolean tienePermisoActivo(Integer idDoctor, Integer idPaciente) {
        List<PermisosHistorial> permisos = permisoHistorialRepository.findAll();
        return permisos.stream()
                .anyMatch(p -> p.getDoctor() != null && p.getPaciente() != null &&
                        p.getDoctor().getIdUsuario().equals(idDoctor) &&
                        p.getPaciente().getIdUsuario().equals(idPaciente) &&
                        p.getEstado() == EstadoPermisosEnum.ACTIVO &&
                        p.getFechaDeniegaPermiso() == null);
    }
    
    public List<PermisosHistorial> getPermisosActivosPorDoctor(Integer idDoctor) {
        return permisoHistorialRepository.findAll().stream()
                .filter(p -> p.getDoctor() != null &&
                        p.getDoctor().getIdUsuario().equals(idDoctor) &&
                        p.getEstado() == EstadoPermisosEnum.ACTIVO &&
                        p.getFechaDeniegaPermiso() == null)
                .collect(Collectors.toList());
    }
    
    public List<PermisosHistorial> getPermisosOtorgadosPorPaciente(Integer idPaciente) {
        return permisoHistorialRepository.findAll().stream()
                .filter(p -> p.getPaciente() != null &&
                        p.getPaciente().getIdUsuario().equals(idPaciente))
                .collect(Collectors.toList());
    }

}