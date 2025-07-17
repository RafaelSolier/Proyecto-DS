package com.habimed.habimedWebService.usuario.domain.service;

import com.habimed.habimedWebService.auth.dto.LoginRequest;
import com.habimed.habimedWebService.auth.dto.RegisterRequest;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.domain.service.PersonaService;
import com.habimed.habimedWebService.persona.dto.PersonaFilterDto;
import com.habimed.habimedWebService.persona.dto.PersonaInsertDto;
import com.habimed.habimedWebService.persona.dto.PersonaResponseDto;
import com.habimed.habimedWebService.persona.dto.PersonaUpdateDto;
import com.habimed.habimedWebService.persona.repository.PersonaRepository;
import com.habimed.habimedWebService.usuario.domain.model.TipoUsuarioEnum;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import com.habimed.habimedWebService.usuario.dto.UsuarioFilterDto;
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
    private final PersonaService personaService;
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

    @Override
    public List<UsuarioResponseDto> findAllWithConditions(UsuarioFilterDto usuarioFilterDto) {
        // IMPLEMENTACIÓN TEMPORAL (reemplazar con consultas personalizadas del repositorio):
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Filtrar por campos del FilterDto si no son null
        if (usuarioFilterDto.getDniPersona() != null) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getPersona() != null &&
                            u.getPersona().getDni().equals(usuarioFilterDto.getDniPersona()))
                    .collect(Collectors.toList());
        }

        if (usuarioFilterDto.getTipoUsuario() != null) {
            // Convertir ID a enum (asumiendo que 1=ADMIN, 2=DOCTOR, 3=PACIENTE, etc.)
            usuarios = usuarios.stream()
                    .filter(u -> u.getTipoUsuario() == usuarioFilterDto.getTipoUsuario())
                    .collect(Collectors.toList());
        }

        if (usuarioFilterDto.getUsuario() != null && !usuarioFilterDto.getUsuario().trim().isEmpty()) {
            String usuarioBuscado = usuarioFilterDto.getUsuario().toLowerCase().trim();
            usuarios = usuarios.stream()
                    .filter(u -> u.getCorreo() != null &&
                            u.getCorreo().toLowerCase().contains(usuarioBuscado))
                    .collect(Collectors.toList());
        }

        if (usuarioFilterDto.getEstado() != null) {
            usuarios = usuarios.stream()
                    .filter(u -> u.getEstado() != null && u.getEstado().equals(usuarioFilterDto.getEstado()))
                    .collect(Collectors.toList());
        }
        List<UsuarioResponseDto> usuariosBuscados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
           UsuarioResponseDto usuarioResponseDto = modelMapper.map(usuario, UsuarioResponseDto.class);
           usuarioResponseDto.setIdPersona(usuario.getPersona().getId());
           usuariosBuscados.add(usuarioResponseDto);
        }
        return usuariosBuscados;
    }

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
            throw new ResourceNotFoundException("No existe una persona con DNI: " + usuarioInsertDto.getIdPersona());
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
            throw new ConflictException("La persona ya tiene un usuario de tipo: " + usuarioInsertDto.getTipoUsuario());
        }

        Usuario usuario = modelMapper.map(usuarioInsertDto, Usuario.class);
        usuario.setPersona(personaEntity);

        // Establecer estado por defecto
        usuario.setEstado(usuarioInsertDto.getEstado() != null ? usuarioInsertDto.getEstado() : false);
        usuario.setIdUsuario(null);
        Usuario _usuario = usuarioRepository.save(usuario);
        return modelMapper.map(_usuario,UsuarioResponseDto.class);
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
            usuarioEntity.setEstado(Boolean.FALSE);
            usuarioRepository.save(usuarioEntity);
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }

    @Override
    public UsuarioResponseDto validarCredenciales(LoginRequest loginRequest) {
        // 1. Buscar usuario en BD
        Usuario usuarioEncontrado = usuarioRepository.findByCorreo((loginRequest.getUsuario()))
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // 2. Verificar existencia y contraseña (sin Spring Security)
        if (usuarioEncontrado != null &&
                usuarioEncontrado.getContrasenia().equals(loginRequest.getContrasenia())) {

            return mapToDto(usuarioEncontrado);
        }
        return null;
    }

    @Override
    public UsuarioResponseDto registrarUsuario(RegisterRequest registerRequest) {
        Persona persona = new Persona();
        // Verificar si la persona existe (por DNI)
        PersonaFilterDto filtros = new PersonaFilterDto();
        filtros.setDni(registerRequest.getDni());
        List<PersonaResponseDto> personas = personaService.findAllWithConditions(filtros);
        // Si existe usar su ID
        if (personas.size() == 1) {
            // Actualizar sus datos
            PersonaUpdateDto personaUpdateDto = modelMapper.map(personas.get(0), PersonaUpdateDto.class);
            personaService.update(personas.get(0).getId(), personaUpdateDto);
            persona = personaRepository.findById(personas.get(0).getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Persona no encontrada. Error al guardar persona."));
        } else if (personas.size() > 1) {
            throw new ConflictException("No se puede registrar el usuario");
        } else {
            // Si no existe crearla y obtener su ID
            Persona nuevaPersona = modelMapper.map(registerRequest, Persona.class);
            nuevaPersona.setId(null);
            persona = personaRepository.save(nuevaPersona);
        }

        UsuarioInsertDto usuarioInsertDto = modelMapper.map(registerRequest, UsuarioInsertDto.class);
        usuarioInsertDto.setIdPersona(persona.getId());
        // Agregar un usuario nuevo con el ID de persona realcionado
        // Verificar que la persona no tenga ya un usuario del mismo tipo
        List<Usuario> usuariosPersona = usuarioRepository.findAll().stream()
                .filter(u -> u.getPersona() != null &&
                        u.getPersona().getId().equals(usuarioInsertDto.getIdPersona()))
                .collect(Collectors.toList());

        boolean tipoUsuarioExiste = usuariosPersona.stream()
                .anyMatch(u -> u.getTipoUsuario() == usuarioInsertDto.getTipoUsuario());

        if (tipoUsuarioExiste) {
            throw new ConflictException("La persona ya tiene un usuario de tipo: " + usuarioInsertDto.getTipoUsuario());
        }

        Usuario usuario = modelMapper.map(usuarioInsertDto, Usuario.class);
        usuario.setPersona(persona);

        // Establecer estado por defecto
        usuario.setEstado(usuarioInsertDto.getEstado() != null ? usuarioInsertDto.getEstado() : false);
        usuario.setIdUsuario(null);
        Usuario _usuario = usuarioRepository.save(usuario);

        return modelMapper.map(_usuario,UsuarioResponseDto.class);
    }

    private UsuarioResponseDto mapToDto(Usuario usuario) {
        UsuarioResponseDto dto = new UsuarioResponseDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setIdPersona(usuario.getPersona().getId());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setCorreo(usuario.getCorreo());
        dto.setEstado(usuario.getEstado());
        if (usuario.getTipoUsuario() == TipoUsuarioEnum.DOCTOR) {
            dto.setCodigoCMP(usuario.getCodigoCMP());
        }
        return dto;
    }

}