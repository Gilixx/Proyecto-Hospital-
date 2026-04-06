package com.example.demo.controller;

import com.example.demo.dto.DoctorRegistrationDTO;
import com.example.demo.model.Especialidad;
import com.example.demo.repository.EspecialidadRepository;
import com.example.demo.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final EspecialidadRepository especialidadRepository;
    private final com.example.demo.service.CitaService citaService;
    private final com.example.demo.service.AuthService authService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        com.example.demo.model.Usuario user = authService.getLoggedUser()
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        
        boolean isAdmin = user.getRol() != null && user.getRol().getNombreRol().equals("ROLE_ADMIN");
        
        model.addAttribute("citas", citaService.getCitasForLoggedDoctor(user));
        model.addAttribute("usuario", user);
        model.addAttribute("isAdmin", isAdmin);
        return "doctor/dashboard";
    }

    @PostMapping("/citas/confirmar")
    public String confirmarCita(@org.springframework.web.bind.annotation.RequestParam("idCita") Integer idCita,
                                RedirectAttributes redirectAttributes) {
        try {
            citaService.confirmarCita(idCita);
            redirectAttributes.addFlashAttribute("exito", "¡Cita confirmada exitosamente!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al confirmar la cita: " + e.getMessage());
        }
        return "redirect:/doctor/dashboard";
    }

    @PostMapping("/citas/reagendar")
    public String reagendarCita(@org.springframework.web.bind.annotation.RequestParam("idCita") Integer idCita,
                                @org.springframework.web.bind.annotation.RequestParam("fecha") java.time.LocalDate fecha,
                                @org.springframework.web.bind.annotation.RequestParam("hora") java.time.LocalTime hora,
                                RedirectAttributes redirectAttributes) {
        try {
            citaService.reagendarCita(idCita, fecha, hora);
            redirectAttributes.addFlashAttribute("exito", "Cita reagendada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reagendar la cita: " + e.getMessage());
        }
        return "redirect:/doctor/dashboard";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("doctorRegistrationDTO", new DoctorRegistrationDTO());
        List<Especialidad> especialidades = especialidadRepository.findAll();
        model.addAttribute("especialidades", especialidades);
        return "doctor/alta";
    }

    @PostMapping("/registro")
    public String registrarDoctor(@Valid @ModelAttribute("doctorRegistrationDTO") DoctorRegistrationDTO dto,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            List<Especialidad> especialidades = especialidadRepository.findAll();
            model.addAttribute("especialidades", especialidades);
            return "doctor/alta";
        }

        try {
            doctorService.registrarDoctor(dto);
            redirectAttributes.addFlashAttribute("exito", "¡Doctor registrado exitosamente!");
            return "redirect:/login"; // O redirigir a un dashboard luego
        } catch (IllegalArgumentException e) {
            result.rejectValue("email", "error.doctorRegistrationDTO", e.getMessage());
            List<Especialidad> especialidades = especialidadRepository.findAll();
            model.addAttribute("especialidades", especialidades);
            return "doctor/alta";
        } catch (RuntimeException e) {
            model.addAttribute("errorGlobal", e.getMessage());
            List<Especialidad> especialidades = especialidadRepository.findAll();
            model.addAttribute("especialidades", especialidades);
            return "doctor/alta";
        }
    }
}
