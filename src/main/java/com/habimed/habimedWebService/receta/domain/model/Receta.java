package com.habimed.habimedWebService.receta.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.habimed.habimedWebService.cita.domain.model.Cita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idreceta")
    private Integer idReceta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcita", referencedColumnName = "idcita")
    //@JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cita cita;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_receta", nullable = false)
    private LocalDate fechaReceta = LocalDate.now();
}
