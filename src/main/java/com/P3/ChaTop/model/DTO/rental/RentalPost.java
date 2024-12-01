package com.P3.ChaTop.model.DTO.rental;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public record RentalPost(
        String name,
        float surface,
        float price,
        MultipartFile picture,
        String description) { }
