package com.example.demo.repository;

import com.example.demo.model.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    List<Especialidad> findByActivaTrue();
}
