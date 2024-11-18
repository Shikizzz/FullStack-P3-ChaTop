package com.P3.ChaTop.model;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank()String name, @NotBlank()String email, @NotBlank()String password) {
}
