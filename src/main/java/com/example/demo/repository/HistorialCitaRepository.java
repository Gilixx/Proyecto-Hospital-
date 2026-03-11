package com.example.demo.repository;

import com.example.demo.model.HistorialCita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialCitaRepository extends JpaRepository<HistorialCita, Integer> {
    List<HistorialCita> findByCitaIdCitaOrderByCambioEnAsc(Integer idCita);
}
