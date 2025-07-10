package com.habimed.habimedWebService.permisoHistorial.application;


import com.habimed.habimedWebService.exception.BadRequestException;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ForbiddenException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import com.habimed.habimedWebService.permisoHistorial.domain.model.PermisosHistorial;
import com.habimed.habimedWebService.permisoHistorial.domain.service.PermisoHistorialService;
import com.habimed.habimedWebService.permisoHistorial.dto.*;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permiso-historial")
@RequiredArgsConstructor
public class PermisoHistorialController {

    private final PermisoHistorialService permisoHistorialService;

    @GetMapping
    public ResponseEntity<List<PermisosHistorial>> getAllPermisosHistorial() {
        try {
            List<PermisosHistorial> permisoHistorialServiceAll = permisoHistorialService.findAll();
            return ResponseEntity.ok(permisoHistorialServiceAll);
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            } else if (e instanceof BadRequestException) {
                throw new BadRequestException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<PermisosHistorial>> getPermisosWithFilter(@Valid @RequestBody PermisoHistorialFilterDto filterDto) {
        try {
            List<PermisosHistorial> responseDtos = permisoHistorialService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(responseDtos);
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            } else if (e instanceof BadRequestException) {
                throw new BadRequestException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermisoHistorialResponseDto> getPermisosById(@PathVariable Integer id) {
        try {
            PermisoHistorialResponseDto especialidad = permisoHistorialService.getById(id);
            if (especialidad != null) {
                return ResponseEntity.ok(especialidad);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            } else if (e instanceof BadRequestException) {
                throw new BadRequestException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<PermisoHistorialResponseDto> createPermisosHistorial(@Valid @RequestBody PermisoHistorialnsertDto permiso) {
        try {
            PermisoHistorialResponseDto createdPermiso = permisoHistorialService.save(permiso);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPermiso);
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            } else if (e instanceof BadRequestException) {
                throw new BadRequestException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PermisoHistorialResponseDto> updatePermiso(
            @PathVariable Integer id,
            @Valid @RequestBody PermisoHistoriaUpdateDto permisoHistoriaUpdateDto) {
        try {
            PermisoHistorialResponseDto updatedPermiso = permisoHistorialService.update(id, permisoHistoriaUpdateDto);
            if (updatedPermiso != null) {
                return ResponseEntity.ok(updatedPermiso);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            } else if (e instanceof BadRequestException) {
                throw new BadRequestException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermiso(@PathVariable Integer id) {
        try {
            Boolean deleted = permisoHistorialService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            } else if (e instanceof BadRequestException) {
                throw new BadRequestException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }
}
