package com.P3.ChaTop.controller;


import com.P3.ChaTop.model.DTO.auth.UserDto;
import com.P3.ChaTop.model.DTO.rental.MessageResponse;
import com.P3.ChaTop.model.DTO.rental.GetAllRentalsResponse;
import com.P3.ChaTop.model.DTO.rental.RentalDTO;
import com.P3.ChaTop.model.DTO.rental.RentalPost;
import com.P3.ChaTop.model.DTO.rental.RentalPut;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Rental Controller", description = "REST Controller used to manage Rentals (CRUD operations)")
@RequestMapping("/api")
public class RentalController {
    private RentalService rentalService;
    private UserService userService;

    public RentalController(RentalService rentalService, UserService userService) {
        this.rentalService = rentalService;
        this.userService = userService;
    }
    @Operation(summary = "Post a Rental", description = "A picture is needeed to create a Rental")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns confirmation message",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "If unable to store the picture. Try with a PNG"),
            @ApiResponse(responseCode = "401", description = "User not logged in",
                    content = {@Content(mediaType = "application/json")})
    })
    @PostMapping(path ="/rentals", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> postRental(@Valid @ModelAttribute RentalPost rentalPost, Authentication authentication) {
        UserDto user = userService.getUserDtoByEmail(authentication.getName());
        Integer id = user.getId();
        boolean isUploaded = rentalService.postRental(rentalPost, id);
        if (isUploaded){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Rental created !"), HttpStatus.OK);
        }
        return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
    }
    @Operation(summary = "Rental modification")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns confirmation message",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "401", description = "User not logged in, or User trying to update another User's Rental",
                    content = {@Content(mediaType = "application/json")})
    })
    @PutMapping(path ="/rentals/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> putRental (@Valid @ModelAttribute RentalPut rentalPut,@Parameter @PathVariable("id") Integer rentalId, Authentication authentication) {
        UserDto user = userService.getUserDtoByEmail(authentication.getName());
        Integer userId = user.getId();   //needed to verify if request is sent by the owner of the Rental
        boolean isUpdated = rentalService.putRental(rentalPut, rentalId ,userId);
        if (isUpdated){
            return new ResponseEntity<MessageResponse>(new MessageResponse("Rental updated !"), HttpStatus.OK);
        }
        return new ResponseEntity<String>("", HttpStatus.UNAUTHORIZED); //unauthorized if user tries to update another user's Rental
    }
    @Operation(summary = "Getting all Rentals in DB")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns all Rentals, as an Array",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GetAllRentalsResponse.class))}),
            @ApiResponse(responseCode = "401", description = "User not logged in",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/rentals")
    public ResponseEntity<?> getAllRentals() {
        GetAllRentalsResponse rentals = rentalService.getAllRentals();
        return new ResponseEntity<GetAllRentalsResponse>(rentals, HttpStatus.OK);
    }

    @Operation(summary = "Getting a Rental details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Returns the Rental details",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RentalDTO.class))}),
            @ApiResponse(responseCode = "401", description = "User not logged in",
                    content = {@Content(mediaType = "application/json")})
    })
    @GetMapping("/rentals/{id}")
    public ResponseEntity<?> getRental(@Parameter @Valid @PathVariable("id") Integer id) { //if not found, returns a RentalDTO with Null attributes
        RentalDTO rental = rentalService.getRentalById(id);
        return new ResponseEntity<RentalDTO>(rental, HttpStatus.OK);
    }

}
