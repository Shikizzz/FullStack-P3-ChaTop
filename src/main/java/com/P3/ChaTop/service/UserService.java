package com.P3.ChaTop.service;

import com.P3.ChaTop.model.DTO.auth.RegisterRequest;
import com.P3.ChaTop.model.DTO.auth.UserDto;
import com.P3.ChaTop.model.User;
import com.P3.ChaTop.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository repository;
    private BCryptPasswordEncoder encoder;
    private ModelMapper modelMapper;

    public UserService(UserRepository repository, BCryptPasswordEncoder encoder, ModelMapper modelMapper) {
        this.repository = repository;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
    }

    public Boolean emailExistsInDB(String email){
        List<User> users = repository.findByEmail(email); //emails are unique
        return users.size()>0;
    }

    public User findByEmail(String email){
        return repository.findByEmail(email).get(0); //emails are unique
    }
    public UserDto getUserDtoByEmail(String email){
        User user = findByEmail(email);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }


    public void registerUser(RegisterRequest registerRequest){
        User user = new User();
        user.setEmail(registerRequest.email());
        user.setName(registerRequest.name());
        user.setPassword(encoder.encode(registerRequest.password()));
        user.setCreated_at(new Timestamp(System.currentTimeMillis()));
        user.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        repository.save(user);
    }

    public UserDto getUserDtoById(Integer id) {
        Optional<User> optionalUser = repository.findById(id);
        User user = optionalUser.orElseGet(User::new);
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }
}
