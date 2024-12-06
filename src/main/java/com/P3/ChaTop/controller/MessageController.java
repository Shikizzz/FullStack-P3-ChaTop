package com.P3.ChaTop.controller;

import com.P3.ChaTop.model.DTO.auth.UserDto;
import com.P3.ChaTop.model.DTO.message.PutMessage;
import com.P3.ChaTop.model.DTO.rental.MessageResponse;
import com.P3.ChaTop.model.DTO.rental.RentalDTO;
import com.P3.ChaTop.service.MessageService;
import com.P3.ChaTop.service.RentalService;
import com.P3.ChaTop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Message Controller", description = "REST Controller used to Post messages to a User, about a Rental")
@RequestMapping("/api")
public class MessageController {
    private final RentalService rentalService;
    private UserService userService;
    private final MessageService messageService;
    public MessageController(RentalService rentalService, UserService userService, MessageService messageService) {
        this.rentalService = rentalService;
        this.userService = userService;
        this.messageService = messageService;
    }
    @Operation(summary = "Post a message", description = "Message created refers to a Rental and its owner's User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns confirmation message",
                content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "If User is trying to send a message to himself"),
            @ApiResponse(responseCode = "401", description = "User not logged in",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping("/messages")
    public ResponseEntity<?> postMessage(@Valid @RequestBody PutMessage message, Authentication authentication) {
        RentalDTO rental = rentalService.getRentalById(message.getRental_id());
        if (rental.getOwner_id().equals(message.getUser_id())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserDto user = userService.getUserDtoByEmail(authentication.getName());
        Integer userId = user.getId();   //needed to verify if user_id is the same as User logged in
        if ( ! message.getUser_id().equals(userId)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        messageService.postMessage(message);
        return new ResponseEntity<>(new MessageResponse("Message send with success"), HttpStatus.OK);
    }
}
