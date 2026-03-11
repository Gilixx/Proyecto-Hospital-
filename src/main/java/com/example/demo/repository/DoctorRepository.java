package com.example.demo.repository;

import com.example.demo.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findByActivoTrue();
    List<Doctor> findByEspecialidadIdEspecialidadAndActivoTrue(Integer idEspecialidad);
    Optional<Doctor> findByUsuarioIdUsuario(Integer idUsuario);
}
