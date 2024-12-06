package com.P3.ChaTop.model.DTO.rental;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RentalPut {
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "Surface cannot be empty")
    @Min(value = 0, message = "Surface should be positive")
    private float surface;
    @NotNull(message = "Price cannot be empty")
    @Min(value = 0, message = "Price should be positive")
    private float price;
    @NotBlank(message = "Description cannot be empty")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSurface() {
        return surface;
    }

    public void setSurface(float surface) {
        this.surface = surface;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
