package com.example.demo.repository;

import com.example.demo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByPacienteIdPacienteOrderByFechaCitaDesc(Integer idPaciente);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.usuario JOIN FETCH c.doctor d JOIN FETCH d.usuario WHERE c.doctor.idDoctor = :idDoctor ORDER BY c.fechaCita ASC")
    List<Cita> findByDoctorIdDoctorOrderByFechaCitaAsc(@Param("idDoctor") Integer idDoctor);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.usuario JOIN FETCH c.doctor d JOIN FETCH d.usuario WHERE c.doctor.idDoctor = :idDoctor " +
           "AND c.fechaCita BETWEEN :inicio AND :fin " +
           "AND c.estado NOT IN ('CANCELADA', 'NO_ASISTIO')")
    List<Cita> findCitasDoctorEnRango(@Param("idDoctor") Integer idDoctor,
                                      @Param("inicio")   LocalDateTime inicio,
                                      @Param("fin")      LocalDateTime fin);

    @Query("SELECT c FROM Cita c JOIN FETCH c.paciente p JOIN FETCH p.usuario JOIN FETCH c.doctor d JOIN FETCH d.usuario ORDER BY c.fechaCita ASC")
    List<Cita> findAllByOrderByFechaCitaAsc();
}
