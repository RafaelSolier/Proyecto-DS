package com.habimed.habimedWebService.usuario.application;

import java.util.List;

import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.usuario.domain.service.UsuarioService;
import com.habimed.habimedWebService.usuario.dto.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    final private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> getAllUsuarios() {
        try {
            List<UsuarioResponseDto> usuarios = usuarioService.findAll();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<UsuarioResponseDto>> getUsuariosWithFilter(@Valid @RequestBody UsuarioFilterDto filterDto) {
        try {
            List<UsuarioResponseDto> usuarios = usuarioService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getUsuarioById(@PathVariable Integer id) {
        try {
            UsuarioResponseDto usuario = usuarioService.getById(id);
            if (usuario != null) {
                return ResponseEntity.ok(usuario);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> createUsuario(@Valid @RequestBody UsuarioInsertDto usuarioInsertDto) {
        try {
            UsuarioResponseDto createdUsuario = usuarioService.save(usuarioInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<UsuarioResponseDto> updateUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateDto usuarioUpdateDto) {
        try {
            UsuarioResponseDto updatedUsuario = usuarioService.update(id, usuarioUpdateDto);
            if (updatedUsuario != null) {
                return ResponseEntity.ok(updatedUsuario);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        try {
            Boolean deleted = usuarioService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}