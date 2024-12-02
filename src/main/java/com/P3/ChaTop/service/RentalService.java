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
import java.util.Date;
import java.util.Map;
import java.util.Optional;

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
            rental.setOwner_id(id);
            Timestamp now = new Timestamp(new Date().getTime());
            rental.setCreated_at(now);
            rental.setUpdated_at(now);
            rentalRepository.save(rental);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public boolean putRental(RentalPut rentalPut, Integer id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        if (rental.isEmpty() || rental.get().getOwner_id()!=id){
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
        Rental[] rentals = rentalRepository.findAll().toArray(new Rental[0]);
        return new GetAllRentalsResponse(rentals);
    }
    public RentalDTO getRentalById(Integer id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        RentalDTO rentalDTO = rental.isPresent() ? modelMapper.map(rental, RentalDTO.class) : new RentalDTO();
        return rentalDTO;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
