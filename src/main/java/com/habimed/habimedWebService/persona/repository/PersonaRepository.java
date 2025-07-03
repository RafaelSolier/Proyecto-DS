package com.habimed.habimedWebService.persona.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.habimed.habimedWebService.persona.domain.model.Persona;


@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

}