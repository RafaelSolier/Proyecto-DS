package com.habimed.habimedWebService.horarioDoctor.repository;

import com.habimed.habimedWebService.horarioDoctor.domain.model.DiaEnum;
import com.habimed.habimedWebService.horarioDoctor.domain.model.HorarioDoctor;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HorarioDoctorRepository extends JpaRepository<HorarioDoctor, Integer> {

   @Query("SELECT COUNT(h) > 0 FROM HorarioDoctor h WHERE h.doctor.idUsuario = :idDoctor " +
       "AND h.diaSemana = :diaSemana AND ((:horaFin > h.horaInicio AND :horaInicio < h.horaFin))")
    boolean existsConflictingHorario(@Param("idDoctor") Integer idDoctor,@Param("diaSemana") DiaEnum diaSemana,
                @Param("horaInicio") LocalDateTime horaInicio,@Param("horaFin") LocalDateTime horaFin);
   
                @Query("SELECT COUNT(h) > 0 FROM HorarioDoctor h " +
       "WHERE h.doctor.idUsuario = :idDoctor " +
       "AND h.diaSemana = :diaSemana " +
       "AND h.idHorarioDoctor <> :idHorarioDoctor " +
       "AND (:horaFin > h.horaInicio AND :horaInicio < h.horaFin)")
boolean existsConflictingHorarioForUpdate(@Param("idDoctor") Integer idDoctor,
                                         @Param("diaSemana") DiaEnum diaSemana,
                                         @Param("horaInicio") LocalDateTime horaInicio,
                                         @Param("horaFin") LocalDateTime horaFin,
                                         @Param("idHorarioDoctor") Integer idHorarioDoctor);
}