package com.P3.ChaTop.model.DTO.rental;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public record RentalPost(
        @NotBlank(message = "Name cannot be empty")
        String name,
        @NotNull(message = "Surface cannot be empty")
        @Min(value = 0, message = "Surface should be positive")
        float surface,
        @NotNull(message = "Price cannot be empty")
        @Min(value = 0, message = "Price should be positive")
        float price,
        @NotNull(message = "You need to add a picture")
        @Schema(description = "The picture, type is File")
        MultipartFile picture,
        @NotBlank(message = "Description cannot be empty")
        String description) { }
