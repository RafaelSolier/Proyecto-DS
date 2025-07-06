package com.habimed.habimedWebService.servicio.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.especialidad.domain.model.Especialidad;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idservicio")
    private Integer idServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idespecialidad", referencedColumnName = "idespecialidad")
    @JsonBackReference
    private Especialidad especialidad;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "riesgos", length = 500)
    private String riesgos;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ConsultorioServicioU> consultorioServicioU;
}