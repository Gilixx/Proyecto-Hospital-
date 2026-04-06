package com.example.demo.dto;

import com.example.demo.model.Usuario.Genero;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DoctorRegistrationDTO {
    
    // User info
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email no válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    private String telefono;
    
    private LocalDate fechaNacimiento;
    
    private Genero genero;

    // Doctor info
    @NotNull(message = "La especialidad es obligatoria")
    private Integer idEspecialidad;

    @NotBlank(message = "La cédula profesional es obligatoria")
    private String cedulaProfesional;

    private String consultorio;

    @NotNull(message = "El costo de la consulta es obligatorio")
    private BigDecimal costoConsulta;

    private String bio;
}
