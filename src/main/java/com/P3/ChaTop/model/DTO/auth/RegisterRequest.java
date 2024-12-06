package com.P3.ChaTop.model.DTO.auth;

import com.P3.ChaTop.config.CustomValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank(message = "Name may not be null")
                              String name,
                              @Email(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
                                      message = "Invalid email")
                              String email,
                              @CustomValidation
                              String password) { }
