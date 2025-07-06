package com.habimed.habimedWebService.consultorioServicioU.aplication;

import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.consultorioServicioU.domain.service.ConsultorioServicioUService;
import com.habimed.habimedWebService.consultorioServicioU.dto.ConsultorioServicioUInsertDto;
import com.habimed.habimedWebService.consultorioServicioU.dto.ConsultorioServicioUResponseDto;
import com.habimed.habimedWebService.consultorioServicioU.dto.FilterConsultorioServicioUDto;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ForbiddenException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conseruser")
@RequiredArgsConstructor
public class ConsultorioServicioUController {
    private final ConsultorioServicioUService consultorioServicioUService;

    @GetMapping
    public ResponseEntity<List<ConsultorioServicioUResponseDto>> listaConsultorioU() {
        return ResponseEntity.ok(consultorioServicioUService.findAll());
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ConsultorioServicioUResponseDto>> buscaConsultorioU(@RequestBody FilterConsultorioServicioUDto filterConsultorioServicioUDto) {
        return ResponseEntity.ok(consultorioServicioUService.findAllWithConditions(filterConsultorioServicioUDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultorioServicioUResponseDto> getConsultorioU(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(consultorioServicioUService.getById(id));
        }catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<ConsultorioServicioU> creadteConsultorioU(@RequestBody ConsultorioServicioUInsertDto consultorioServicioUInsertDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(consultorioServicioUService.save(consultorioServicioUInsertDto));
        } catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsultorioU(@PathVariable Integer id) {
        try {
            consultorioServicioUService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConsultorioServicioU> updateConsultorioU(@PathVariable Integer id,
                                                                   @RequestBody ConsultorioServicioUInsertDto consultorioServicioUInsertDto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(consultorioServicioUService.update(id, consultorioServicioUInsertDto));
        }catch (Exception e) {
            if (e instanceof ConflictException) {
                throw new ConflictException(e.getMessage());
            } else if (e instanceof ForbiddenException) {
                throw new ForbiddenException(e.getMessage());
            } else if (e instanceof ResourceNotFoundException) {
                throw new ResourceNotFoundException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage());
        }
    }
}

