package com.habimed.habimedWebService.consultorioServicioU.repository;

import com.habimed.habimedWebService.consultorioServicioU.domain.model.ConsultorioServicioU;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioServicioURepository extends JpaRepository<ConsultorioServicioU, Integer> {
}
