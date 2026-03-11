package com.example.demo.repository;

import com.example.demo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByPacienteIdPacienteOrderByFechaCitaDesc(Integer idPaciente);

    List<Cita> findByDoctorIdDoctorOrderByFechaCitaAsc(Integer idDoctor);

    @Query("SELECT c FROM Cita c WHERE c.doctor.idDoctor = :idDoctor " +
           "AND c.fechaCita BETWEEN :inicio AND :fin " +
           "AND c.estado NOT IN ('CANCELADA', 'NO_ASISTIO')")
    List<Cita> findCitasDoctorEnRango(@Param("idDoctor") Integer idDoctor,
                                      @Param("inicio")   LocalDateTime inicio,
                                      @Param("fin")      LocalDateTime fin);

    @Query("SELECT c FROM Cita c WHERE c.estado = 'PENDIENTE' " +
           "AND c.fechaCita BETWEEN :inicio AND :fin")
    List<Cita> findCitasPendientesEnRango(@Param("inicio") LocalDateTime inicio,
                                          @Param("fin")    LocalDateTime fin);
}
