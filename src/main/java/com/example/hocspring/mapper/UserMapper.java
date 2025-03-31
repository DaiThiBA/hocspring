package com.example.hocspring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.hocspring.dto.request.UserCreationRequest;
import com.example.hocspring.dto.request.UserUpdationRequest;
import com.example.hocspring.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest req);

    void updateUser(@MappingTarget User user, UserUpdationRequest req);

}
