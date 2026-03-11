package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueos_calendario")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BloqueoCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bloqueo")
    private Integer idBloqueo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(length = 200)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private TipoBloqueo tipo;

    public enum TipoBloqueo {
        VACACIONES, CAPACITACION, URGENCIA, FESTIVO
    }
}
