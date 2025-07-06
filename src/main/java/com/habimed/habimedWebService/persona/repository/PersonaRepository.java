package com.habimed.habimedWebService.persona.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.habimed.habimedWebService.persona.domain.model.Persona;

import java.util.List;


@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {
    List<Persona> findByDni(String dni);
}