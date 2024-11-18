package com.P3.ChaTop.repository;

import com.P3.ChaTop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findByEmail(String email);
}

