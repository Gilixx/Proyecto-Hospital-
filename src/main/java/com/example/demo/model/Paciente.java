package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pacientes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer idPaciente;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "num_expediente", unique = true, length = 20)
    private String numExpediente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_sangre", length = 10)
    private TipoSangre tipoSangre;

    @Column(columnDefinition = "TEXT")
    private String alergias;

    @Column(name = "enfermedades_cronicas", columnDefinition = "TEXT")
    private String enfermedadesCronicas;

    @Column(name = "seguro_medico", length = 100)
    private String seguroMedico;

    @Column(name = "contacto_emergencia", length = 150)
    private String contactoEmergencia;

    @Column(name = "tel_emergencia", length = 20)
    private String telEmergencia;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    public enum TipoSangre {
        A_POS, A_NEG, B_POS, B_NEG, AB_POS, AB_NEG, O_POS, O_NEG
    }
}
