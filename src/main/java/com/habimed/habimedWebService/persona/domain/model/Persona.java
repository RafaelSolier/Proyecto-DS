package com.habimed.habimedWebService.persona.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.habimed.habimedWebService.usuario.domain.model.Usuario;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {

    @Id
    @Column(name = "idpersona")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dni", nullable = false, length = 10)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 45)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 45)
    private String apellidos;

    @Column(name = "celular", length = 9)
    private String celular;

    @Column(name = "direccion", length = 45)
    private String direccion;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    // Relación inversa One-to-Many con Usuario
    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Usuario> usuarios;
}
