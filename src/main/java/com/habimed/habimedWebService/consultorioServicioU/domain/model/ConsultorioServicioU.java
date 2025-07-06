package com.habimed.habimedWebService.consultorioServicioU.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class ConsultorioServicioU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idconsultorioserviciouser")
    private Integer idConsultorioServicioU;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    @JsonBackReference
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idconsultorio", referencedColumnName = "idconsultorio")
    @JsonBackReference
    private Consultorio consultorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio", referencedColumnName = "idservicio")
    @JsonBackReference
    private Servicio servicio;

    @OneToMany(mappedBy = "consultorioServicioU", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Cita> citas;

}
