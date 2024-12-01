package com.P3.ChaTop.model.DTO.rental;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Timestamp;

public class RentalDTO {
    private Integer id;
    private String email;
    private String name;
    private String password;
    private Timestamp created_at;
    private Timestamp updated_at;
}
