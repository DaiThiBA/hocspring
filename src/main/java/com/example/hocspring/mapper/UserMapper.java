package com.example.hocspring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.hocspring.dto.request.UserCreationRequest;
import com.example.hocspring.dto.request.UserUpdationRequest;
import com.example.hocspring.dto.response.UserResponse;
import com.example.hocspring.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest req);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdationRequest req);

    UserResponse toUserResponse(User user);
}
