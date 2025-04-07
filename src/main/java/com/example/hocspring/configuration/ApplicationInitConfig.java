package com.example.hocspring.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.hocspring.entity.User;
import com.example.hocspring.enums.Role;
import com.example.hocspring.repository.UserRepository;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class ApplicationInitConfig {
    
    BCryptPasswordEncoder passwordEncoder;
    UserRepository userRepository;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()) {
                var roles = new HashSet<String>();
                roles.add(Role.ADMIN.name());
                
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        //.roles(roles)
                        .build();
                userRepository.save(user);

                log.warn("admin user has been created with password: admin, please change it!");
            }
        };

}}
