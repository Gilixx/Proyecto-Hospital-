package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especialidades")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "duracion_cita_min", nullable = false)
    @Builder.Default
    private Short duracionCitaMin = 30;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activa = true;
}
