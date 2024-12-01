package com.P3.ChaTop.service;

import com.P3.ChaTop.model.User;
import com.P3.ChaTop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService {
    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Boolean emailExistsInDB(String email){
        List<User> users = repository.findByEmail(email); //emails are unique
        return users.size()>0;
    }
    public User findByEmail(String email){
        return repository.findByEmail(email).get(0); //emails are unique
    }

    public void addUsertoDB(User user){
        user.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        repository.save(user);
    }
}
