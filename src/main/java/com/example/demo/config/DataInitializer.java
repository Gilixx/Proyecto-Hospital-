package com.example.demo.config;

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

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!usuarioRepository.existsByEmail("123@gmail.com")) {
            Rol rolPaciente = rolRepository.findByNombreRol("PACIENTE")
                    .orElseGet(() -> {
                        Rol r = new Rol();
                        r.setNombreRol("PACIENTE");
                        r.setDescripcion("Paciente registrado");
                        return rolRepository.save(r);
                    });

            Usuario newUsuario = new Usuario();
            newUsuario.setNombre("Prueba");
            newUsuario.setApellidos("Hospital");
            newUsuario.setEmail("123@gmail.com");
            newUsuario.setPasswordHash(passwordEncoder.encode("eltin2404"));
            newUsuario.setRol(rolPaciente);
            newUsuario.setActivo(true);
            newUsuario.setGenero(Usuario.Genero.OTRO);
            newUsuario.setFechaNacimiento(LocalDate.of(2000, 1, 1));
            newUsuario.setTelefono("555-0000");

            usuarioRepository.save(newUsuario);
            System.out.println("==============================================");
            System.out.println("Usuario 123@gmail.com creado correctamente!!!");
            System.out.println("==============================================");
        }
    }
}
