package com.example.hocspring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.hocspring.dto.request.RoleRequest;
import com.example.hocspring.dto.response.RoleResponse;
import com.example.hocspring.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)// tại vì permissions là 1 list nên không thể map trực tiếp được, mà phải map tự map
    Role toRole (RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
