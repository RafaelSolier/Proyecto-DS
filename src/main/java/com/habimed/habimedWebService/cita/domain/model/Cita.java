package com.habimed.habimedWebService.cita.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.detallePago.domain.model.DetallePago;
import com.habimed.habimedWebService.diagnostico.domain.model.Diagnostico;
import com.habimed.habimedWebService.receta.domain.model.Receta;
import com.habimed.habimedWebService.recomendacion.domain.model.Recomendacion;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcita")
    private Integer idCita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpaciente", referencedColumnName = "idusuario")
    @JsonBackReference("cita-paciente")
    private Usuario paciente;

    @Column(name = "motivo", nullable = false, length = 255)
    private String motivo;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;

    @Column(name = "fecha_hora_fin")
    private LocalDateTime fechaHoraFin;

    @Column(name = "estado", nullable = false)
    private EstadoCitaEnum estado;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "motivoCancelacion", length = 500)
    private String motivoCancelacion;

    // Relaciones One-to-Many
    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diagnostico> diagnosticos;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Receta> recetas;

    @OneToMany(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Recomendacion> recomendaciones;

    // Relación One-to-One con DetallePago
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private DetallePago detallePago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idconsultorioserviciouser", referencedColumnName = "idconsultorioserviciouser", nullable = false)
    @JsonBackReference
    private ConsultorioServicioU consultorioServicioU;
}