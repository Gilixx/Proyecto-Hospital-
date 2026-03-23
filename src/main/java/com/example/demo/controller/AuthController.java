package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String homeRedirect(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                if (auth.getAuthority().equals("ROLE_MEDICO")) {
                    return "redirect:/doctor/calendario";
                } else if (auth.getAuthority().equals("ROLE_PACIENTE")) {
                    return "redirect:/paciente/agendar";
                } else if (auth.getAuthority().equals("ROLE_ADMIN")) {
                    return "redirect:/admin/dashboard";
                }
            }
        }
        return "redirect:/login";
    }
}
