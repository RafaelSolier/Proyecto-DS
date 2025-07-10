package com.habimed.habimedWebService.resenia.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resenia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idresenia")
    private Integer idResenia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddoctor", referencedColumnName = "idusuario")
    //@JsonBackReference
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario doctor;

    @Column(name = "calificacion", nullable = false)
    private Double calificacion;

    @Column(name = "comentario", length = 1000)
    private String comentario;

    @Column(name = "fecha_resenia", nullable = false)
    private LocalDate fechaResenia = LocalDate.now();
}
