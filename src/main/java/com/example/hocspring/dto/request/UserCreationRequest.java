package com.example.hocspring.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.example.hocspring.validator.DobContraints;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min =3 , message = "INVALID_USERNAME")
    String username;
    @Size(min =8 , message = "INVALID_PASSWORD")
    String password;
    String firstName;
    String lastName;

    @DobContraints(min = 16, message = "INVALID_DOB")
    LocalDate dob;
    Set<String> roles;
}
