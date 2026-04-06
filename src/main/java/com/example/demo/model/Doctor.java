package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "doctores")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_doctor")
    private Integer idDoctor;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    @Column(name = "cedula_profesional", unique = true, length = 30)
    private String cedulaProfesional;

    @Column(length = 20)
    private String consultorio;

    @Column(name = "costo_consulta", precision = 8, scale = 2)
    private BigDecimal costoConsulta;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
