package com.example.demo.repository;

import com.example.demo.model.BloqueoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BloqueoCalendarioRepository extends JpaRepository<BloqueoCalendario, Integer> {

    @Query("SELECT b FROM BloqueoCalendario b WHERE b.doctor.idDoctor = :idDoctor " +
           "AND b.fechaFin >= :ahora ORDER BY b.fechaInicio ASC")
    List<BloqueoCalendario> findVigentesByDoctor(@Param("idDoctor") Integer idDoctor,
                                                 @Param("ahora")    LocalDateTime ahora);
}
