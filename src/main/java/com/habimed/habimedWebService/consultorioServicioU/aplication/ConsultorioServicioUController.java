package com.habimed.habimedWebService.consultorioServicioU.aplication;

import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.consultorioServicioU.domain.service.ConsultorioServicioUService;
import com.habimed.habimedWebService.consultorioServicioU.dto.ConsultorioServicioUInsertDto;
import com.habimed.habimedWebService.consultorioServicioU.dto.FilterConsultorioServicioUDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conseruser")
@RequiredArgsConstructor
public class ConsultorioServicioUController {
    private final ConsultorioServicioUService consultorioServicioUService;

    @GetMapping
    public ResponseEntity<List<ConsultorioServicioU>> listaConsultorioU() {
        return ResponseEntity.ok(consultorioServicioUService.findAll());
    }

    @GetMapping
    public ResponseEntity<List<ConsultorioServicioU>> buscaConsultorioU(@RequestBody FilterConsultorioServicioUDto filterConsultorioServicioUDto) {
        return ResponseEntity.ok(consultorioServicioUService.findAllWithConditions(filterConsultorioServicioUDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultorioServicioU> getConsultorioU(@PathVariable Integer id) {
        return ResponseEntity.ok(consultorioServicioUService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ConsultorioServicioU> creadteConsultorioU(@RequestBody ConsultorioServicioUInsertDto consultorioServicioUInsertDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultorioServicioUService.save(consultorioServicioUInsertDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteConsultorioU(@PathVariable Integer id) {
        return ResponseEntity.ok(consultorioServicioUService.delete(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConsultorioServicioU> updateConsultorioU(@PathVariable Integer id,
                                                                   @RequestBody ConsultorioServicioUInsertDto consultorioServicioUInsertDto) {
        return ResponseEntity.status(HttpStatus.OK).body(consultorioServicioUService.update(id,consultorioServicioUInsertDto));
    }
}

