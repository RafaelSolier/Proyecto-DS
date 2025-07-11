package com.habimed.habimedWebService.detallePago.application;

import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.exception.BadRequestException;
import com.habimed.habimedWebService.exception.ConflictException;
import com.habimed.habimedWebService.exception.ForbiddenException;
import com.habimed.habimedWebService.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.habimed.habimedWebService.detallePago.domain.service.DetallePagoService;
import com.habimed.habimedWebService.detallePago.dto.*;

import java.util.List;
@RestController
@RequestMapping("/api/detalle-pagos")
@RequiredArgsConstructor
public class DetallePagoController {

    private final DetallePagoService detallePagoService;

    @GetMapping
    public ResponseEntity<List<DetallePago>> getAllDetallePagos() {
        try {
            List<DetallePago> detallePagos = detallePagoService.findAll();
            return ResponseEntity.ok(detallePagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<List<DetallePagoResponseDto>> getDetallePagosWithFilter(@Valid @RequestBody DetallePagoFilterDto filterDto) {
        try {
            List<DetallePagoResponseDto> detallePagos = detallePagoService.findAllWithConditions(filterDto);
            return ResponseEntity.ok(detallePagos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePagoResponseDto> getDetallePagoById(@PathVariable Integer id) {
        try {
            DetallePagoResponseDto detallePago = detallePagoService.getById(id);
            if (detallePago != null) {
                return ResponseEntity.ok(detallePago);
            } else {
                return ResponseEntity.notFound().build();
            }
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

    @PostMapping
    public ResponseEntity<DetallePagoResponseDto> createDetallePago(@Valid @RequestBody DetallePagoInsertDto detallePagoInsertDto) {
        try {
            DetallePagoResponseDto createdDetallePago = detallePagoService.save(detallePagoInsertDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDetallePago);
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

    @PatchMapping("/{id}")
    public ResponseEntity<DetallePagoResponseDto> updateDetallePago(
            @PathVariable Integer id,
            @Valid @RequestBody DetallePagoUpdateDto detallePagoUpdateDto) {
        try {
            DetallePagoResponseDto updatedDetallePago = detallePagoService.update(id, detallePagoUpdateDto);
            if (updatedDetallePago != null) {
                return ResponseEntity.ok(updatedDetallePago);
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
    public ResponseEntity<Void> deleteDetallePago(@PathVariable Integer id) {
        try {
            Boolean deleted = detallePagoService.delete(id);
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