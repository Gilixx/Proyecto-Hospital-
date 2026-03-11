package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "horarios_disponibles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HorarioDisponible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Integer idHorario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    /** 0=Dom, 1=Lun, 2=Mar, 3=Mié, 4=Jue, 5=Vie, 6=Sáb */
    @Column(name = "dia_semana", nullable = false)
    private Byte diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "intervalo_min", nullable = false)
    @Builder.Default
    private Short intervaloMin = 30;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}
