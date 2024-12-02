package com.P3.ChaTop.controller;

import com.P3.ChaTop.model.DTO.message.PutMessage;
import com.P3.ChaTop.model.DTO.rental.MessageResponse;
import com.P3.ChaTop.model.DTO.rental.RentalDTO;
import com.P3.ChaTop.model.User;
import com.P3.ChaTop.service.MessageService;
import com.P3.ChaTop.service.RentalService;
import com.P3.ChaTop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @PostMapping("/messages/") //Todo add Validation
    public ResponseEntity<?> postMessage(@RequestBody PutMessage message, Authentication authentication) {
        RentalDTO rental = rentalService.getRentalById(message.getRental_id());
        if (rental.getOwner_id().equals(message.getUser_id())){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User user = userService.findByEmail(authentication.getName());
        Integer userId = user.getId();   //needed to verify if user_id is the same as User logged in
        if ( ! message.getUser_id().equals(userId)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        messageService.postMessage(message);
        return new ResponseEntity<>(new MessageResponse("Message send with success"), HttpStatus.OK);
    }
}
