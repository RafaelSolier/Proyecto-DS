package com.habimed.habimedWebService.usuario.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.habimed.habimedWebService.cita.domain.model.Cita;
import com.habimed.habimedWebService.consultorio.domain.model.Consultorio;
import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;
import com.habimed.habimedWebService.persona.domain.model.Persona;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer idUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idpersona", nullable = false)
    @JsonBackReference("usuario-persona")
    private Persona persona;

    @Column(name = "tipousuario", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoUsuarioEnum tipoUsuario;

    @Column(name = "correo", nullable = false, length = 50, unique = true)
    private String correo;

    @Column(name = "contrasenia", nullable = false)
    private String contrasenia;

    @Column(name = "estado", nullable = false)          // true = Cuenta activa, false = usuario eliminado
    private Boolean estado = true;

    @Column(name = "codigo_cmp", unique = true, nullable = true, length = 20)
    private String codigoCMP; // Código del Colegio Médico del Perú

    // Relaciones inversas
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonBackReference
    private List<HorarioDoctor> horarios;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("cita-paciente")
    private List<Cita> citasComoPaciente;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ConsultorioServicioU> consultorioServicioU;
}