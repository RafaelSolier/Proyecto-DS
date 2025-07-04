package com.habimed.habimedWebService.usuario.domain.service;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.UsuarioInsertDto;
import com.habimed.habimedWebService.usuario.dto.UsuarioResponseDto;
import com.habimed.habimedWebService.usuario.dto.UsuarioUpdateDto;
import com.habimed.habimedWebService.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PersonaRepository personaRepository;
    private final ModelMapper modelMapper;
    // Opcional: Para encriptar contraseñas
    // private final PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioResponseDto> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDto> responseDto = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            UsuarioResponseDto usuarioResponseDto = modelMapper.map(usuario, UsuarioResponseDto.class);
            usuarioResponseDto.setIdPersona(usuario.getPersona().getId());
            responseDto.add(usuarioResponseDto);
        }
        return  responseDto;
    }

//    @Override
//    public List<UsuarioResponseDto> findAllWithConditions(UsuarioFilterDto usuarioFilterDto) {
//        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
//        List<Usuario> usuarios = usuarioRepository.findAll();
//
//        // Filtrar por campos del FilterDto si no son null
//        if (usuarioFilterDto.getDniPersona() != null) {
//            usuarios = usuarios.stream()
//                    .filter(u -> u.getPersona() != null &&
//                            u.getPersona().getDni().equals(usuarioFilterDto.getDniPersona()))
//                    .collect(Collectors.toList());
//        }
//
//        if (usuarioFilterDto.getTipoUsuarioId() != null) {
//            // Convertir ID a enum (asumiendo que 1=ADMIN, 2=DOCTOR, 3=PACIENTE, etc.)
//            TipoUsuarioEnum tipoUsuario = convertirIdATipoUsuario(usuarioFilterDto.getTipoUsuarioId());
//            if (tipoUsuario != null) {
//                usuarios = usuarios.stream()
//                        .filter(u -> u.getTipoUsuario() == tipoUsuario)
//                        .collect(Collectors.toList());
//            }
//        }
//
//        if (usuarioFilterDto.getUsuario() != null && !usuarioFilterDto.getUsuario().trim().isEmpty()) {
//            String usuarioBuscado = usuarioFilterDto.getUsuario().toLowerCase().trim();
//            usuarios = usuarios.stream()
//                    .filter(u -> u.getCorreo() != null &&
//                            u.getCorreo().toLowerCase().contains(usuarioBuscado))
//                    .collect(Collectors.toList());
//        }
//
//        if (usuarioFilterDto.getEstado() != null) {
//            usuarios = usuarios.stream()
//                    .filter(u -> u.getEstado() != null && u.getEstado().equals(usuarioFilterDto.getEstado()))
//                    .collect(Collectors.toList());
//        }
//
//        return usuarios;
//    }

    @Override
    public UsuarioResponseDto getById(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            UsuarioResponseDto usuarioResponseDto = modelMapper.map(usuario.get(), UsuarioResponseDto.class);
            usuarioResponseDto.setIdPersona(usuario.get().getPersona().getId());
            return usuarioResponseDto;
        }
        throw new RuntimeException("Usuario no encontrado con ID: " + id);
    }

    @Override
    public UsuarioResponseDto save(UsuarioInsertDto usuarioInsertDto) {

        // Verificar que la persona existe
        Optional<Persona> persona = personaRepository.findById(usuarioInsertDto.getIdPersona());
        if (!persona.isPresent()) {
            throw new RuntimeException("No existe una persona con DNI: " + usuarioInsertDto.getIdPersona());
        }
        
        Persona personaEntity = persona.get();
        
        // Verificar que la persona no tenga ya un usuario del mismo tipo
        List<Usuario> usuariosPersona = usuarioRepository.findAll().stream()
                .filter(u -> u.getPersona() != null && 
                        u.getPersona().getId().equals(usuarioInsertDto.getIdPersona()))
                .collect(Collectors.toList());

        boolean tipoUsuarioExiste = usuariosPersona.stream()
                .anyMatch(u -> u.getTipoUsuario() == usuarioInsertDto.getTipoUsuario());
        
        if (tipoUsuarioExiste) {
            throw new RuntimeException("La persona ya tiene un usuario de tipo: " + usuarioInsertDto.getTipoUsuario());
        }

        Usuario usuario = modelMapper.map(usuarioInsertDto, Usuario.class);
        usuario.setPersona(personaEntity);

        // Establecer estado por defecto
        usuario.setEstado(usuarioInsertDto.getEstado() != null ? usuarioInsertDto.getEstado() : false);

        return modelMapper.map(usuarioRepository.save(usuario), UsuarioResponseDto.class);
    }

    @Override
    public UsuarioResponseDto update(Integer id, UsuarioUpdateDto usuarioUpdateDto) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);

        if (existingUsuario.isPresent()) {
            Usuario usuario = existingUsuario.get();

            // Actualizar solo los campos que no son null en el DTO
            if (usuario.getTipoUsuario() == TipoUsuarioEnum.DOCTOR) {
                if (usuarioUpdateDto.getCodigoCMP() != null && !usuarioUpdateDto.getCodigoCMP().trim().isEmpty()) {

                    // Verificar que el nuevo código CMP no esté ya registrado por otro usuario
                    boolean codigoCMP = usuarioRepository.findAll().stream()
                            .anyMatch(u -> !u.getIdUsuario().equals(id) &&
                                    u.getCodigoCMP() != null &&
                                    u.getCodigoCMP().toLowerCase().equals(usuarioUpdateDto.getCodigoCMP().toLowerCase().trim()));

                    if (codigoCMP) {
                        throw new RuntimeException("Ya existe otro usuario registrado con ese código CMP: ");
                    }

                    usuario.setCodigoCMP(usuarioUpdateDto.getCodigoCMP().toLowerCase().trim());
                }
            }
            
            if (usuarioUpdateDto.getContrasenia() != null && !usuarioUpdateDto.getContrasenia().trim().isEmpty()) {

                usuario.setContrasenia(usuarioUpdateDto.getContrasenia()); // Temporal sin encriptar

            }
            
            if (usuarioUpdateDto.getEstado() != null) {
                
                usuario.setEstado(usuarioUpdateDto.getEstado());
            }
            
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            UsuarioResponseDto usuarioResponseDto = modelMapper.map(updatedUsuario, UsuarioResponseDto.class);
            usuarioResponseDto.setIdPersona(updatedUsuario.getPersona().getId());
            return usuarioResponseDto;
        }
        
        throw new RuntimeException("Usuario no encontrado con ID: " + id);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        
        if (usuario.isPresent()) {
            Usuario usuarioEntity = usuario.get();
            
            // Verificar que no sea el último administrador
            if (usuarioEntity.getTipoUsuario() == TipoUsuarioEnum.ADMIN) {
                long administradoresActivos = usuarioRepository.findAll().stream()
                        .filter(u -> u.getTipoUsuario() == TipoUsuarioEnum.ADMIN && 
                                u.getEstado() != null && u.getEstado())
                        .count();
                
                if (administradoresActivos <= 1) {
                    throw new RuntimeException("No se puede eliminar el último administrador activo del sistema");
                }
            }

            usuarioRepository.deleteById(id);
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }

    

}