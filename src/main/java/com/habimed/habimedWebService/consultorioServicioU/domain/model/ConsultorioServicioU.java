package com.habimed.habimedWebService.consultorioServicioU.domain.model;

import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.servicio.domain.model.Servicio;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class ConsultorioServicioU {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsultorioServicioU;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", referencedColumnName = "idusuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idconsultorio", referencedColumnName = "idconsultorio")
    private Consultorio consultorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idservicio", referencedColumnName = "idservicio")
    private Servicio servicio;
}
