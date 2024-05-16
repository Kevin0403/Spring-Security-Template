package org.example.memoryauthentication.controller;


import org.example.memoryauthentication.dto.AuthRequest;
import org.example.memoryauthentication.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        Authentication authRequestTocken = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authRequestTocken);

        if (authentication.isAuthenticated()) {
            return jwtService.generateTocken(authentication);
        }
        else {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}
