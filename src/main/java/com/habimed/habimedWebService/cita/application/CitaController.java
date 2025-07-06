package com.habimed.habimedWebService.cita.application;

import java.util.List;

import com.habimed.habimedWebService.cita.dto.*;
import com.habimed.habimedWebService.exception.BadRequestException;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ForbiddenException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.cita.domain.service.CitaService;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @GetMapping
    public ResponseEntity<List<CitaResponseDto>> getAllCitas() {
        try {
            List<CitaResponseDto> citas = citaService.findAll();
            return ResponseEntity.ok(citas);
        }catch (Exception e) {
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
    public ResponseEntity<List<CitaResponseDto>> getAllCitasWithConditions(@RequestBody CitaFilterDto citaFilterDto) {
        List<CitaResponseDto> citas = citaService.findAllWithConditions(citaFilterDto);
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponseDto> getCitaById(@PathVariable Integer id) {
        CitaResponseDto cita = citaService.getById(id);
        return ResponseEntity.ok(cita);
    }

    @PostMapping
    public ResponseEntity<CitaResponseDto> createCita(@Valid @RequestBody CitaInsertDto citaInsertDto) {
        try{
            CitaResponseDto citaCreada = citaService.save(citaInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(citaCreada);
        }
        catch (Exception e) {
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
    public ResponseEntity<CitaResponseDto> updateCita(
            @PathVariable Integer id,
            @Valid @RequestBody CitaUpdateDto citaUpdateDto) {
        try {
            CitaResponseDto citaActualizada = citaService.update(id, citaUpdateDto);
            return ResponseEntity.ok(citaActualizada);
        }catch (Exception e) {
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
    public ResponseEntity<Void> deleteCita(@PathVariable Integer id) {
        Boolean deleted = citaService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}