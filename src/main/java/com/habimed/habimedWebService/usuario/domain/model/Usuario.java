package com.habimed.habimedWebService.usuario.domain.model;

import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import com.habimed.habimedWebService.persona.domain.model.Persona;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpersona", referencedColumnName = "id", insertable = false, updatable = false)
    private Persona persona;

    @Column(name = "tipousuario", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuarioEnum tipoUsuario;

    @Column(name = "correo", nullable = false, length = 50)
    private String correo;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Column(name = "estado", nullable = false)
    private Boolean estado = false;

    @Column(name = "codigo_cmp", unique = true, nullable = true, length = 20)
    private String codigoCMP; // Código del Colegio Médico del Perú

    // Relaciones inversas
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HorarioDoctor> horarios;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citasComoPaciente;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cita> citasComoDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idconsultorio", referencedColumnName = "idconsultorio", insertable = false, updatable = false, nullable = true)
    private Consultorio consultorio;

    // Falta Relación con
}