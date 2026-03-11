package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "citas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Integer idCita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_doctor", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private Especialidad especialidad;

    @Column(name = "fecha_cita", nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "hora_fin_estimada")
    private LocalDateTime horaFinEstimada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    @Builder.Default
    private Estado estado = Estado.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta", nullable = false, length = 15)
    @Builder.Default
    private TipoConsulta tipoConsulta = TipoConsulta.PRESENCIAL;

    @Column(name = "motivo_consulta", columnDefinition = "TEXT")
    private String motivoConsulta;

    @Column(name = "notas_internas", columnDefinition = "TEXT")
    private String notasInternas;

    @Column(name = "confirmada_en")
    private LocalDateTime confirmadaEn;

    @CreationTimestamp
    @Column(name = "creada_en", nullable = false, updatable = false)
    private LocalDateTime creadaEn;

    public enum Estado {
        PENDIENTE, CONFIRMADA, CANCELADA, COMPLETADA, NO_ASISTIO
    }

    public enum TipoConsulta {
        PRESENCIAL, TELEMEDICINA, URGENCIA
    }
}
