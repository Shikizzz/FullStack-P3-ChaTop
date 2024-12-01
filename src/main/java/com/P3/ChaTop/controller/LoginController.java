package com.P3.ChaTop.controller;

import com.P3.ChaTop.model.DTO.auth.LoginRequest;
import com.P3.ChaTop.model.DTO.auth.RegisterRequest;
import com.P3.ChaTop.model.DTO.auth.Token;
import com.P3.ChaTop.model.DTO.UserDto;
import com.P3.ChaTop.model.User;
import com.P3.ChaTop.service.JWTService;
import com.P3.ChaTop.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class LoginController {
    private JWTService jwtService;
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private BCryptPasswordEncoder encoder;
    private ModelMapper modelMapper;

    public LoginController(JWTService jwtService, AuthenticationManager authenticationManager, UserService userService, BCryptPasswordEncoder encoder, ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.login(), loginRequest.password()));
        String token = jwtService.generateToken(authentication);
        return new ResponseEntity<Token>(new Token(token), HttpStatus.OK);
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
        return new ResponseEntity<Token>(new Token(token), HttpStatus.OK);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserDto> getMyUser(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

}
