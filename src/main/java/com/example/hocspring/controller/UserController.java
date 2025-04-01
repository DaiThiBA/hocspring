package com.example.hocspring.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.hocspring.dto.request.UserCreationRequest;
import com.example.hocspring.dto.request.UserUpdationRequest;
import com.example.hocspring.dto.response.ApiResponse;
import com.example.hocspring.dto.response.UserResponse;
import com.example.hocspring.entity.User;
import com.example.hocspring.service.UserService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@Slf4j
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
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("User name: {} ", authentication.getName());
        authentication.getAuthorities().forEach(grantAuthority -> log.info(grantAuthority.getAuthority()));

        return userService.getUser(userId);
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        
        return ApiResponse.<UserResponse>builder()
        .result(userService.getMyInfo())
        .build();
    }

    @GetMapping()
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
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
