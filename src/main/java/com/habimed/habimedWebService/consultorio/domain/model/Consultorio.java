package com.habimed.habimedWebService.consultorio.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Consultorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idconsultorio")
    private Integer idConsultorio;

    @Column(name = "ruc", length = 11)
    private String ruc;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "direccion", length = 45)
    private String direccion;

    @Column(name = "telefono", length = 9)
    private String telefono;

    @Column(name = "estado", nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean estado = true; // true=activo, false=eliminado lógico

    @OneToMany(mappedBy = "consultorio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ConsultorioServicioU> consultorioServicioU;

}