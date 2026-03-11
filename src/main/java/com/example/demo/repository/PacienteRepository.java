package com.example.demo.repository;

import com.example.demo.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
    Optional<Paciente> findByUsuarioIdUsuario(Integer idUsuario);
    Optional<Paciente> findByNumExpediente(String numExpediente);
}
