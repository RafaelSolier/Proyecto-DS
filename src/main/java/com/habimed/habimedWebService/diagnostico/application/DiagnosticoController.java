package com.habimed.habimedWebService.diagnostico.application;

import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.diagnostico.dto.*;
import com.habimed.habimedWebService.diagnostico.dto.DiagnosticoResponseDto;
import com.habimed.habimedWebService.exception.BadRequestException;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ForbiddenException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.diagnostico.domain.service.DiagnosticoService;

import java.util.List;

@RestController
@RequestMapping("/api/diagnosticos")
@RequiredArgsConstructor
public class DiagnosticoController {

    private final DiagnosticoService diagnosticoService;

    @GetMapping
    public ResponseEntity<List<DiagnosticoResponseDto>> getAllDiagnosticos() {
        try {
            List<DiagnosticoResponseDto> diagnosticos = diagnosticoService.findAll();
            return ResponseEntity.ok(diagnosticos);
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

//    @PostMapping("/filter")
//    public ResponseEntity<List<Diagnostico>> getDiagnosticosWithFilter(@Valid @RequestBody DiagnosticoFilterDto filterDto) {
//        try {
//            List<Diagnostico> diagnosticos = diagnosticoService.findAllWithConditions(filterDto);
//            return ResponseEntity.ok(diagnosticos);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosticoResponseDto> getDiagnosticoById(@PathVariable Integer id) {
        try {
            DiagnosticoResponseDto diagnostico = diagnosticoService.getById(id);
            if (diagnostico != null) {
                return ResponseEntity.ok(diagnostico);
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
    public ResponseEntity<DiagnosticoResponseDto> createDiagnostico(@Valid @RequestBody DiagnosticoInsertDto diagnosticoInsertDto) {
        try {
            DiagnosticoResponseDto createdDiagnostico = diagnosticoService.save(diagnosticoInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDiagnostico);
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
    public ResponseEntity<DiagnosticoResponseDto> updateDiagnostico(
            @PathVariable Integer id,
            @Valid @RequestBody DiagnosticoUpdateDto diagnosticoUpdateDto) {
        try {
            DiagnosticoResponseDto updatedDiagnostico = diagnosticoService.update(id, diagnosticoUpdateDto);
            if (updatedDiagnostico != null) {
                return ResponseEntity.ok(updatedDiagnostico);
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
    public ResponseEntity<Void> deleteDiagnostico(@PathVariable Integer id) {
        try {
            Boolean deleted = diagnosticoService.delete(id);
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