package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CitaDTO {
    private Integer idDoctor;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivoConsulta;
}
