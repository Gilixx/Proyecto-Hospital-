package com.example.demo.repository;

import com.example.demo.model.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Integer> {
    List<HorarioDisponible> findByDoctorIdDoctorAndActivoTrue(Integer idDoctor);
    List<HorarioDisponible> findByDoctorIdDoctorAndDiaSemanaAndActivoTrue(Integer idDoctor, Byte diaSemana);
}
