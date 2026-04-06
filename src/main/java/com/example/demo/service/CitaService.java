package com.example.demo.service;

import com.example.demo.model.Cita;
import com.example.demo.model.Doctor;
import com.example.demo.repository.CitaRepository;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;
    private final PacienteRepository pacienteRepository;

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Cita> getCitasForDoctor(Integer idDoctor) {
        return citaRepository.findByDoctorIdDoctorOrderByFechaCitaAsc(idDoctor);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Cita> getCitasForLoggedDoctor(com.example.demo.model.Usuario usuario) {
        // Si el usuario es administrador, devolver todas las citas del sistema
        if (usuario.getRol() != null && usuario.getRol().getNombreRol().equals("ROLE_ADMIN")) {
            return citaRepository.findAllByOrderByFechaCitaAsc();
        }
        
        Doctor doctor = doctorRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene un perfil de doctor asociado."));
        return citaRepository.findByDoctorIdDoctorOrderByFechaCitaAsc(doctor.getIdDoctor());
    }

    @org.springframework.transaction.annotation.Transactional
    public void confirmarCita(Integer idCita) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        cita.setEstado(Cita.Estado.CONFIRMADA);
        cita.setConfirmadaEn(LocalDateTime.now());
        citaRepository.save(cita);
    }

    @org.springframework.transaction.annotation.Transactional
    public void reagendarCita(Integer idCita, java.time.LocalDate fecha, java.time.LocalTime hora) {
        Cita cita = citaRepository.findById(idCita)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));
        
        LocalDateTime nuevaFecha = LocalDateTime.of(fecha, hora);
        cita.setFechaCita(nuevaFecha);
        cita.setHoraFinEstimada(nuevaFecha.plusMinutes(cita.getDoctor().getEspecialidad().getDuracionCitaMin()));
        cita.setEstado(Cita.Estado.PENDIENTE); // Al reagendar vuelve a estar pendiente de confirmar
        citaRepository.save(cita);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @org.springframework.transaction.annotation.Transactional
    public Cita agendarCita(com.example.demo.dto.CitaDTO dto, com.example.demo.model.Usuario usuario) {
        
        // Obtener el doctor
        Doctor doctor = doctorRepository.findById(dto.getIdDoctor())
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado"));
                
        // Resolver Paciente
        com.example.demo.model.Paciente paciente = pacienteRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElseGet(() -> {
                    com.example.demo.model.Paciente p = new com.example.demo.model.Paciente();
                    p.setUsuario(usuario);
                    p.setNumExpediente("EXP-" + System.currentTimeMillis());
                    return pacienteRepository.save(p);
                });
        
        // Crear cita
        Cita cita = Cita.builder()
                .paciente(paciente)
                .doctor(doctor)
                .especialidad(doctor.getEspecialidad())
                .fechaCita(LocalDateTime.of(dto.getFecha(), dto.getHora()))
                .horaFinEstimada(LocalDateTime.of(dto.getFecha(), dto.getHora().plusMinutes(doctor.getEspecialidad().getDuracionCitaMin())))
                .estado(Cita.Estado.PENDIENTE)
                .tipoConsulta(Cita.TipoConsulta.PRESENCIAL)
                .motivoConsulta(dto.getMotivoConsulta())
                .build();
                
        return citaRepository.save(cita);
    }
}
