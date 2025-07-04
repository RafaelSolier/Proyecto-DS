package com.habimed.habimedWebService.consultorioServicioU.domain.service;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorio.repository.ConsultorioRepository;
import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.consultorioServicioU.dto.ConsultorioServicioUInsertDto;
import com.habimed.habimedWebService.consultorioServicioU.dto.FilterConsultorioServicioUDto;
import com.habimed.habimedWebService.consultorioServicioU.repository.ConsultorioServicioURepository;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.servicio.repository.ServicioRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultorioServicioUServiceImpl implements ConsultorioServicioUService {
    private final ConsultorioServicioURepository consultorioServicioURepository;
    private final UsuarioRepository usuarioRepository;
    private final ConsultorioRepository consultorioRepository;
    private final ServicioRepository servicioRepository;
    private final ModelMapper modelMapper;
    // Opcional: Para encriptar contraseñas
    // private final PasswordEncoder passwordEncoder;

    @Override
    public List<ConsultorioServicioU> findAll() {
        return consultorioServicioURepository.findAll();
    }

    @Override
    public List<ConsultorioServicioU> findAllWithConditions(FilterConsultorioServicioUDto filterConsultorioServicioUDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<ConsultorioServicioU> buscando = this.findAll();

        // Filtrar por campos del FilterDto si no son null
        if (filterConsultorioServicioUDto.getIdConsultorio() != null) {

            buscando = buscando.stream()
                    .filter(u -> u.getConsultorio() != null &&
                            u.getConsultorio().getIdConsultorio().equals(filterConsultorioServicioUDto.getIdConsultorio()))
                    .collect(Collectors.toList());
        }


        if (filterConsultorioServicioUDto.getIdServicio() != null) {
            buscando = buscando.stream()
                    .filter(u -> u.getServicio() != null &&
                            u.getServicio().getIdServicio().equals(filterConsultorioServicioUDto.getIdServicio()))
                    .collect(Collectors.toList());
        }

        if (filterConsultorioServicioUDto.getIdUsuario() != null) {

            buscando = buscando.stream()
                    .filter(u -> u.getUsuario() != null &&
                            u.getUsuario().getIdUsuario().equals(filterConsultorioServicioUDto.getIdUsuario()))
                    .collect(Collectors.toList());
        }

        return buscando;
    }

    @Override
    public ConsultorioServicioU getById(Integer id) {
        Optional<ConsultorioServicioU> target_ = consultorioServicioURepository.findById(id);
        if (target_.isPresent()) {
            return target_.get();
        }
        throw new RuntimeException("Relacion no encontrada con ID: " + id);
    }

    @Override
    public ConsultorioServicioU save(ConsultorioServicioUInsertDto consultorioServicioUInsertDto) {
        // Verificar que el usuario existe
        Optional<Usuario> usuario = usuarioRepository.findById(consultorioServicioUInsertDto.getIdUsuario());
        if (!usuario.isPresent()) {
            throw new RuntimeException("No existe un usuario con id: " + consultorioServicioUInsertDto.getIdUsuario());
        }

        // Verificar que el usuario sea del tipo DOCTOR
        if (!(usuario.get().getTipoUsuario() == TipoUsuarioEnum.DOCTOR)) {
            throw new RuntimeException("El usuario no es un DOCTOR, sino un : " + usuario.get().getTipoUsuario());
        }

        // Verificar que el consultorio existe
        Optional<Consultorio> consultorio = consultorioRepository.findById(consultorioServicioUInsertDto.getIdConsultorio());
        if (!consultorio.isPresent()) {
            throw new RuntimeException("No existe el consultorio con id: " + consultorioServicioUInsertDto.getIdConsultorio());
        }

        // Verificar que el consultorio existe
        Optional<Servicio> servicio = servicioRepository.findById(consultorioServicioUInsertDto.getIdServicio());
        if (!servicio.isPresent()) {
            throw new RuntimeException("No existe el servicio con id: " + consultorioServicioUInsertDto.getIdServicio());
        }

        // Guardamos la nueva relación
        ConsultorioServicioU u = new ConsultorioServicioU();
        u.setConsultorio(consultorio.get());
        u.setServicio(servicio.get());
        u.setUsuario(usuario.get());
        u.setIdConsultorioServicioU(null);
        consultorioServicioURepository.save(u);

        return u;
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<ConsultorioServicioU> target_ = consultorioServicioURepository.findById(id);

        if (target_.isPresent()) {

            consultorioServicioURepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public ConsultorioServicioU update(Integer id, ConsultorioServicioUInsertDto consultorioServicioUUpdateDto) {
        Optional<ConsultorioServicioU> target_ = consultorioServicioURepository.findById(id);
        
        if (target_.isPresent()) {
            ConsultorioServicioU target = target_.get();

            // Actualizar solo los campos que no son null en el DTO
            if (consultorioServicioUUpdateDto.getIdConsultorio() != null) {

                // Verificar que el consultorio existe
                Optional<Consultorio> consultorio = consultorioRepository.findById(consultorioServicioUUpdateDto.getIdConsultorio());
                if (!consultorio.isPresent()) {
                    throw new RuntimeException("El consultorio no existe. Id: " + consultorioServicioUUpdateDto.getIdConsultorio());
                }
                target.setConsultorio(consultorio.get());


            }
            if (consultorioServicioUUpdateDto.getIdServicio() != null) {

                // Verificar que el servicio existe
                Optional<Servicio> servicio = servicioRepository.findById(consultorioServicioUUpdateDto.getIdServicio());
                if (!servicio.isPresent()) {
                    throw new RuntimeException("El servicio no existe. Id: " + consultorioServicioUUpdateDto.getIdServicio());
                }
                target.setServicio(servicio.get());
            }


            if (consultorioServicioUUpdateDto.getIdUsuario() != null) {

                // Verificar que el usuario existe
                Optional<Usuario> usuario = usuarioRepository.findById(consultorioServicioUUpdateDto.getIdUsuario());
                if (!usuario.isPresent()) {
                    throw new RuntimeException("El usuario no existe. Id: " + consultorioServicioUUpdateDto.getIdUsuario());
                }
                target.setUsuario(usuario.get());
            }


            return consultorioServicioURepository.save(target);
        }
        
        throw new RuntimeException("La relacion no existe con ID: " + id);
    }

}