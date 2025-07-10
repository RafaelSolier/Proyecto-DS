package com.habimed.habimedWebService.permisoHistorial.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisosHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpermisohistorial")
    private Integer idPermisoHistorial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iddoctor", referencedColumnName = "idusuario")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpaciente", referencedColumnName = "idusuario")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Usuario paciente;

    @Column(name = "fechaotorgapermiso", nullable = false)
    private LocalDate fechaOtorgaPermiso = LocalDate.now();

    @Column(name = "fechadeniegapermiso")
    private LocalDate fechaDeniegaPermiso;

    @Column(name = "estado", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private EstadoPermisosEnum estado;
}
