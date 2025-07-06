package com.habimed.habimedWebService.especialidad.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idespecialidad")
    private Integer idEspecialidad;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @OneToMany(mappedBy = "especialidad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Servicio> servicios;
}