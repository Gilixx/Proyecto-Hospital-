package com.example.demo.config;

import com.example.demo.model.Especialidad;
import com.example.demo.repository.EspecialidadRepository;
import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.repository.RolRepository;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final EspecialidadRepository especialidadRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // Crear roles básicos si no existen
        Rol rolPaciente = rolRepository.findByNombreRol("PACIENTE")
                .orElseGet(() -> {
                    Rol r = new Rol();
                    r.setNombreRol("PACIENTE");
                    r.setDescripcion("Paciente registrado");
                    return rolRepository.save(r);
                });

        Rol rolMedico = rolRepository.findByNombreRol("ROLE_MEDICO")
                .orElseGet(() -> {
                    Rol r = new Rol();
                    r.setNombreRol("ROLE_MEDICO");
                    r.setDescripcion("Doctor del hospital");
                    return rolRepository.save(r);
                });
                
        Rol rolAdmin = rolRepository.findByNombreRol("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol r = new Rol();
                    r.setNombreRol("ROLE_ADMIN");
                    r.setDescripcion("Administrador del sistema");
                    return rolRepository.save(r);
                });

        // Crear especialidades básicas si no existen
        if (especialidadRepository.count() == 0) {
            List<String> nombresEspecialidades = Arrays.asList(
                "Medicina General",
                "Pediatría",
                "Cardiología",
                "Dermatología",
                "Ginecología",
                "Ortopedia",
                "Oftalmología",
                "Psiquiatría",
                "Neurología",
                "Nutrición"
            );

            for (String nombre : nombresEspecialidades) {
                Especialidad especialidad = Especialidad.builder()
                        .nombre(nombre)
                        .descripcion("Atención en " + nombre)
                        .duracionCitaMin((short) 30)
                        .activa(true)
                        .build();
                especialidadRepository.save(especialidad);
            }
            System.out.println("==============================================");
            System.out.println("Especialidades médicas insertadas!!");
            System.out.println("==============================================");
        }

        // Crear o actualizar usuario de prueba
        if (!usuarioRepository.existsByEmail("123@gmail.com")) {
            Usuario newUsuario = new Usuario();
            newUsuario.setNombre("Prueba Admin");
            newUsuario.setApellidos("Hospital");
            newUsuario.setEmail("123@gmail.com");
            newUsuario.setPasswordHash(passwordEncoder.encode("eltin2404"));
            newUsuario.setRol(rolAdmin);
            newUsuario.setActivo(true);
            newUsuario.setGenero(Usuario.Genero.OTRO);
            newUsuario.setFechaNacimiento(LocalDate.of(2000, 1, 1));
            newUsuario.setTelefono("555-0000");

            usuarioRepository.save(newUsuario);
            System.out.println("==============================================");
            System.out.println("Usuario Admin 123@gmail.com creado correctamente!!!");
            System.out.println("==============================================");
        } else {
            Usuario admin = usuarioRepository.findByEmail("123@gmail.com").get();
            admin.setRol(rolAdmin);
            usuarioRepository.save(admin);
            System.out.println("==============================================");
            System.out.println("Usuario 123@gmail.com actualizado a ADMIN!!!");
            System.out.println("==============================================");
        }
    }
}
