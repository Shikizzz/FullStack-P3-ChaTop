package com.P3.ChaTop.model;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="rentals")
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="name")
    private String name;
    @Column(name="surface")
    private float surface;
    @Column(name="price")
    private float price;
    @Column(name="picture")
    private String picture; //url of the picture
    @Column(name="description")
    private String description;
    @Column(name="owner_id")
    private Integer owner_id;
    @Column(name="created_at")
    private Timestamp created_at;
    @Column(name="updated_at")
    private Timestamp updated_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
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
