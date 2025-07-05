package com.habimed.habimedWebService.persona.application;

import com.habimed.habimedWebService.persona.domain.model.Persona;
import com.habimed.habimedWebService.persona.domain.service.PersonaService;

import com.habimed.habimedWebService.persona.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Para devolver la respuesta HTTP completa
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonasController {

    private final PersonaService personaService;

    @GetMapping
    public ResponseEntity<List<PersonaResponseDto>> getAllPersonas() {
        try {
            List<PersonaResponseDto> personas = personaService.findAll();
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<PersonaResponseDto>> getPersonasWithFilter(@Valid @RequestBody PersonaFilterDto filterDto) {
        try {
            List<PersonaResponseDto> personas = personaService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(personas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Persona> getPersonaByDni(@PathVariable Integer id) {
        try {
            Persona persona = personaService.getById(id);
            if (persona != null) {
                return ResponseEntity.ok(persona);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<PersonaResponseDto> createPersona(@Valid @RequestBody PersonaInsertDto personaInsertDto) {
        try {
            PersonaResponseDto createdPersona = personaService.save(personaInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPersona);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PersonaResponseDto> updatePersona(
            @PathVariable Integer id,
            @Valid @RequestBody PersonaUpdateDto personaUpdateDto) {
        try {
            PersonaResponseDto updatedPersona = personaService.update(id, personaUpdateDto);
            if (updatedPersona != null) {
                return ResponseEntity.ok(updatedPersona);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersona(@PathVariable Integer id) {
        try {
            Boolean deleted = personaService.delete(id);
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