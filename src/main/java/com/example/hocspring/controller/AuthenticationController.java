package com.example.hocspring.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.hocspring.dto.request.AuthenticationRequest;
import com.example.hocspring.dto.request.IntrospectRequest;
import com.example.hocspring.dto.response.ApiResponse;
import com.example.hocspring.dto.response.AuthenticationResponse;
import com.example.hocspring.dto.response.IntrospectResponse;
import com.example.hocspring.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;


    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        // Tạo đối tượng response
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(); 
        //biến true false
        authenticationResponse = authenticationService.authenticate(authenticationRequest);
    
        //thêm token vào

        // Trả về response
        return ApiResponse.<AuthenticationResponse>builder()
            .code(1000)
            .result(authenticationResponse)
            .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        
        return ApiResponse.<IntrospectResponse>builder()
            .code(1000)
            .result(authenticationService.introspect(request))
            .build();
    }
}
