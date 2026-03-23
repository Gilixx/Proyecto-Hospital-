package com.example.demo.service;

import com.example.demo.model.Cita;
import com.example.demo.model.Doctor;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;

    public List<Cita> getCitasForDoctor(Integer idDoctor) {
        return citaRepository.findAll().stream()
                .filter(c -> c.getDoctor().getIdDoctor().equals(idDoctor))
                .toList();
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
