package com.example.demo.service;

import com.example.demo.dto.DoctorRegistrationDTO;
import com.example.demo.model.Doctor;
import com.example.demo.model.Especialidad;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.EspecialidadRepository;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final UsuarioRepository usuarioRepository;
    private final DoctorRepository doctorRepository;
    private final RolRepository rolRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Doctor registrarDoctor(DoctorRegistrationDTO dto) {
        // Verificar si el email ya existe
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado");
        }

        // Obtener Rol Médico
        Rol rolMedico = rolRepository.findByNombreRol("ROLE_MEDICO")
                .orElseThrow(() -> new RuntimeException("Error: No se encontró el rol ROLE_MEDICO."));

        // Obtener Especialidad
        Especialidad especialidad = especialidadRepository.findById(dto.getIdEspecialidad())
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no válida"));

        // Crear y guardar Usuario
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .telefono(dto.getTelefono())
                .fechaNacimiento(dto.getFechaNacimiento())
                .genero(dto.getGenero())
                .rol(rolMedico)
                .activo(true)
                .build();
        
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        // Crear y guardar Doctor
        Doctor doctor = Doctor.builder()
                .usuario(usuarioGuardado)
                .especialidad(especialidad)
                .cedulaProfesional(dto.getCedulaProfesional())
                .consultorio(dto.getConsultorio())
                .costoConsulta(dto.getCostoConsulta())
                .bio(dto.getBio())
                .activo(true)
                .build();

        return doctorRepository.save(doctor);
    }
}
