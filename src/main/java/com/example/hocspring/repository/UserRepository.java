package com.example.hocspring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hocspring.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    
    boolean existsByUsername(String username);

    //User getByUsernameUser(String username);

    Optional<User> findByUsername(String username);
}
