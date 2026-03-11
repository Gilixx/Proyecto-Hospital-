package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_citas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HistorialCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cita", nullable = false)
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario_accion", nullable = false)
    private Usuario usuarioAccion;

    @Column(name = "estado_anterior", length = 30)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable = false, length = 30)
    private String estadoNuevo;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @CreationTimestamp
    @Column(name = "cambio_en", nullable = false, updatable = false)
    private LocalDateTime cambioEn;
}
