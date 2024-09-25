package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.model.Role;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class SimpleController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/hello-user")
    public String getMethodName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "hello, " + auth.getName() + auth.getAuthorities().toString();
    }

    /*
     * O ideal é que a lógica de cadastramento não seja feita por um controller
     * No entanto, por questões de simplificação está sendo feita aqui mesmo
     * Este endpoint foi criado com o intuito de testar o funcionamento do PasswordEncoder bean
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerRequest.password);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        Usuario usuario = new Usuario();
        usuario.setNomeUsuario(registerRequest.username);
        usuario.setSenha(encryptedPassword);
        usuario.setRoles(roles);
        usuarioRepository.save(usuario);
        return new ResponseEntity<String>("Usuario cadastrado!", HttpStatus.OK);
    }

    public record RegisterRequest(String username, String password) {
	}

    /*
     * JSON:
     * {"username":"rafael","password":"123"}
     * para localhost:8080/register (POST)
     */
    
}
