package com.P3.ChaTop.model;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest (@NotBlank()String email, @NotBlank()String password) {
}
