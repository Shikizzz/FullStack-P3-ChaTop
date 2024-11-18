package com.P3.ChaTop.controller;

import com.P3.ChaTop.model.LoginRequest;
import com.P3.ChaTop.model.RegisterRequest;
import com.P3.ChaTop.model.User;
import com.P3.ChaTop.service.JWTService;
import com.P3.ChaTop.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
public class LoginController {
    private JWTService jwtService;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private BCryptPasswordEncoder encoder;

    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager, UserService userService, BCryptPasswordEncoder encoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.encoder = encoder;
    }

    @PostMapping("/auth/login")
    public String login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        String token = jwtService.generateToken(authentication);
        return token;
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest){
        if(userService.emailExistsInDB(registerRequest.email())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEmail(registerRequest.email());
        user.setName(registerRequest.name());
        user.setPassword(encoder.encode(registerRequest.password()));
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        userService.addUsertoDB(user);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerRequest.email(), registerRequest.password()));
        String token = jwtService.generateToken(authentication);
        return new ResponseEntity<String>(token, HttpStatus.OK);
    }

    @GetMapping("/auth/me")
    public String getMyUser() {
        return "It works";
    }

}
