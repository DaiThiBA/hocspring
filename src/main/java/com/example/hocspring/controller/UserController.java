package com.example.hocspring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.hocspring.dto.request.UserCreationRequest;
import com.example.hocspring.dto.request.UserUpdationRequest;
import com.example.hocspring.dto.response.ApiResponse;
import com.example.hocspring.entity.User;
import com.example.hocspring.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping()
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest req) {
        return userService.createUser(req);
    }

    @GetMapping("/{userId}")
    public ApiResponse<User> getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    public User updateUser(@RequestBody UserUpdationRequest req) {
        return userService.updatUser(req);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        
        userService.deleteUser(userId);
        return "User data deleted successfully";
    }
    

}
