package com.example.demo.controller;

import com.example.demo.model.Cita;
import com.example.demo.model.Doctor;
import com.example.demo.model.Usuario;
import com.example.demo.service.AuthService;
import com.example.demo.service.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;
    private final AuthService authService;

    @GetMapping("/paciente/agendar")
    public String agendarCita(Model model) {
        List<Doctor> doctores = citaService.getAllDoctors();
        model.addAttribute("doctores", doctores);
        return "paciente/agendar";
    }

    @org.springframework.web.bind.annotation.PostMapping("/paciente/agendar")
    public String guardarCita(@org.springframework.web.bind.annotation.ModelAttribute com.example.demo.dto.CitaDTO citaDTO,
                              org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            Optional<Usuario> usuarioOpt = authService.getLoggedUser();
            if (usuarioOpt.isEmpty()) {
                return "redirect:/login";
            }
            citaService.agendarCita(citaDTO, usuarioOpt.get());
            redirectAttributes.addFlashAttribute("exito", "¡Cita agendada con éxito! Revisa el calendario abajo.");
            
            return "redirect:/doctor/calendario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agendar la cita: " + e.getMessage());
            return "redirect:/paciente/agendar";
        }
    }

    @GetMapping("/doctor/calendario")
    public String verCalendario() {
        return "doctor/calendario";
    }

    @GetMapping("/api/citas/mis-citas")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getMisCitas() {
        Optional<Usuario> usuarioOpt = authService.getLoggedUser();
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }
        
        // Asumiendo que podemos obtener el doctor a partir del usuario en sesión
        // En una implementación real se cambiaría por la query correcta
        Integer doctorId = 1; // HARDCODED FOR NOW
        
        List<Cita> citas = citaService.getCitasForDoctor(doctorId);
        List<Map<String, Object>> eventos = citas.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getIdCita());
            map.put("title", "Cita - Estado: " + c.getEstado());
            map.put("start", c.getFechaCita().toString());
            if (c.getHoraFinEstimada() != null) {
                map.put("end", c.getHoraFinEstimada().toString());
            }
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(eventos);
    }
}
