package com.P3.ChaTop.controller;

import com.P3.ChaTop.model.DTO.auth.LoginRequest;
import com.P3.ChaTop.model.DTO.auth.RegisterRequest;
import com.P3.ChaTop.model.DTO.auth.Token;
import com.P3.ChaTop.model.DTO.auth.UserDto;
import com.P3.ChaTop.model.DTO.rental.MessageResponse;
import com.P3.ChaTop.model.DTO.rental.RentalDTO;
import com.P3.ChaTop.model.Message;
import com.P3.ChaTop.service.JWTService;
import com.P3.ChaTop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "User Controller", description = "REST Controller used to manage User (Register, Login and GET requests)")
@RequestMapping("/api")
public class UserController {
    private JWTService jwtService;
    private UserService userService;


    public UserController(JWTService jwtService, UserService userService, ModelMapper modelMapper) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Operation(summary = "Login request", description = "Enter email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns a JWT Bearer token",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "Wrong credentials",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = jwtService.authenticate(loginRequest.email(), loginRequest.password());
        if (token.length()>0){
            return new ResponseEntity<Token>(new Token(token), HttpStatus.OK);
        }
        else return new ResponseEntity<MessageResponse>(new MessageResponse("error"), HttpStatus.OK);
    }
    @Operation(summary = "Register request", description = "Enter name, email and password")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Adds User to DB, and returns a JWT Bearer token",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Email already taken")
    })
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest){
        if(userService.emailExistsInDB(registerRequest.email())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        userService.registerUser(registerRequest);
        String token = jwtService.authenticate(registerRequest.email(), registerRequest.password());
        return new ResponseEntity<Token>(new Token(token), HttpStatus.OK);
    }
    @Operation(summary = "Get authenticated User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns User's info",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/auth/me")
    public ResponseEntity<UserDto> getMyUser(Authentication authentication) {
        UserDto userDto = userService.getUserDtoByEmail(authentication.getName());
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }
    @Operation(summary = "Get user by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns User's info",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))}),
            @ApiResponse(responseCode = "401", description = "Unauthenticated",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@Parameter @PathVariable Integer id){
        UserDto userDto = userService.getUserDtoById(id);
        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

}
