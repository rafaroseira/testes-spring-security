package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class SimpleController {

    @GetMapping("/username")
    public String displayUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @GetMapping("/roles")
    public String displayRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().toString();
    }

    @GetMapping("/principal")
    public String displayPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getPrincipal().toString();
    }

    @GetMapping("/auth-class")
    public String displayAuthClass() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getClass().toString();
    }
}
