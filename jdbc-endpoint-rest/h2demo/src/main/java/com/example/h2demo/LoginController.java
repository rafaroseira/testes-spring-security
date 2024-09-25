package com.example.h2demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class LoginController {

    private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    @Autowired
	private final AuthenticationManager authenticationManager;

	public LoginController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {
		Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
		Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);
		SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authenticationResponse); 
        securityContextHolderStrategy.setContext(context);
        securityContextRepository.saveContext(context, request, response);
            
        return new ResponseEntity<String>("usuario logado!", HttpStatus.OK);
	}

    @GetMapping("/hello-user")
    public String helloUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "hello, " + auth.getName();
    }

    @GetMapping("/hello-admin")
    public String helloAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "hello, " + auth.getName();
    }
    
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "hello world!";
    }
    
	public record LoginRequest(String username, String password) {
	}

}