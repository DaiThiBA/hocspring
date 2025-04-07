package com.example.hocspring.mapper;

import org.mapstruct.Mapper;

import com.example.hocspring.dto.request.PermissionRequest;
import com.example.hocspring.dto.response.PermissionResponse;
import com.example.hocspring.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission (PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
