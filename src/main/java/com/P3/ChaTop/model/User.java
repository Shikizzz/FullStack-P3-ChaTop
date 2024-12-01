package com.P3.ChaTop.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="email")
    private String email;
    @Column(name="name")
    private String name;
    @Column(name="password")
    private String password;
    @Column(name="created_at")
    private Timestamp created_at;
    @Column(name="updated_at")
    private Timestamp updated_at;

    public User(){};
    public User(String email, String name, String password, Timestamp created_at, Timestamp updated_at) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {this.id = id;}
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Timestamp getCreated_at() {
        return created_at;
    }
    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    public Timestamp getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }
}
