package com.P3.ChaTop.controller;


import com.P3.ChaTop.model.DTO.Message;
import com.P3.ChaTop.model.DTO.rental.GetAllRentalsResponse;
import com.P3.ChaTop.model.DTO.rental.RentalDTO;
import com.P3.ChaTop.model.DTO.rental.RentalPost;
import com.P3.ChaTop.model.User;
import com.P3.ChaTop.service.RentalService;
import com.P3.ChaTop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RentalController {
    private RentalService rentalService;
    private UserService userService;

    public RentalController(RentalService rentalService, UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }

    @PostMapping(path ="/rentals", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }) //Todo add Validation
    public ResponseEntity<?> postRental(@ModelAttribute RentalPost rentalPost, Authentication authentication) {
        User user = userService.findByEmail(authentication.getName());
        Integer id = user.getId();
        boolean isUploaded = rentalService.postRental(rentalPost, id);
        if (isUploaded){
            return new ResponseEntity<Message>(new Message("Rental created !"), HttpStatus.OK);
        }
        return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/rentals")
    public ResponseEntity<?> getAllRentals() {
        GetAllRentalsResponse rentals = rentalService.getAllRentals();
        return new ResponseEntity<GetAllRentalsResponse>(rentals, HttpStatus.OK);
    }
    @GetMapping("/rentals/{id}") //Todo debug [org.springframework.web.HttpMediaTypeNotAcceptableException: No acceptable representation]
    public ResponseEntity<?> getRental(@PathVariable("id") int id) {
        RentalDTO rental = rentalService.getRentalById(id);
        return new ResponseEntity<RentalDTO>(rental, HttpStatus.OK);
    }

}
