package com.P3.ChaTop.service;

import com.P3.ChaTop.model.DTO.rental.GetAllRentalsResponse;
import com.P3.ChaTop.model.DTO.rental.RentalDTO;
import com.P3.ChaTop.model.DTO.rental.RentalPost;
import com.P3.ChaTop.model.DTO.rental.RentalPut;
import com.P3.ChaTop.model.Rental;
import com.P3.ChaTop.repository.RentalRepository;
import com.P3.ChaTop.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class RentalService {
    private RentalRepository rentalRepository;
    private Cloudinary cloudinary;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;

    public RentalService(RentalRepository rentalRepository, Cloudinary cloudinary,
                         UserRepository userRepository, ModelMapper modelMapper) {
        this.rentalRepository = rentalRepository;
        this.cloudinary = cloudinary;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public boolean postRental(RentalPost rentalPost, Integer id) {
        try {
            File picture = convertMultiPartToFile(rentalPost.picture());
            Map uploadResult = cloudinary.uploader().upload(picture, ObjectUtils.emptyMap()); //saving picture on Cloudinary
            Rental rental = new Rental();  //creating rental instance to store in DB
            rental.setName(rentalPost.name());
            rental.setSurface(rentalPost.surface());
            rental.setPrice(rentalPost.price());
            rental.setPicture(uploadResult.get("url").toString());
            rental.setDescription(rentalPost.description());
            rental.setUser(userRepository.findById(id).get()); //is present because get from DB is controller
            Timestamp now = new Timestamp(new Date().getTime());
            rental.setCreated_at(now);
            rental.setUpdated_at(now);
            rentalRepository.save(rental);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean putRental(RentalPut rentalPut,Integer rentalId,  Integer userId) {
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isEmpty() || rental.get().getUser().getId().equals(userId)){ //Verify if user is the Rental's poster
            return false;
        }
        Rental updatedRental = rental.get();
        updatedRental.setName(rentalPut.getName());
        updatedRental.setSurface(rentalPut.getSurface());
        updatedRental.setPrice(rentalPut.getPrice());
        updatedRental.setDescription(rentalPut.getDescription());
        updatedRental.setUpdated_at(new Timestamp(new Date().getTime()));
        rentalRepository.save(updatedRental);
        return true;
    }
    public GetAllRentalsResponse getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();//toArray(new Rental[0]);
        List<RentalDTO> rentalsDTO = new ArrayList<>();
        rentals.forEach(rental -> {
                RentalDTO rentalDto = modelMapper.map(rental, RentalDTO.class);
                rentalDto.setOwner_id(rental.getUser().getId());
                rentalsDTO.add(rentalDto);
               });
        return new GetAllRentalsResponse(rentalsDTO.toArray(new RentalDTO[0]));
    }
    public RentalDTO getRentalById(Integer id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        if(rental.isPresent()){
            RentalDTO rentalDTO = modelMapper.map(rental, RentalDTO.class);
            rentalDTO.setOwner_id(rental.get().getUser().getId());
            return rentalDTO;
        }
        return new RentalDTO(); //else
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
