package com.example.hocspring.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.hocspring.dto.request.RoleRequest;
import com.example.hocspring.dto.response.RoleResponse;
import com.example.hocspring.exception.AppException;
import com.example.hocspring.exception.ErrorCode;
import com.example.hocspring.mapper.RoleMapper;
import com.example.hocspring.repository.PermissionRepository;
import com.example.hocspring.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)

@Slf4j
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

   public RoleResponse create(RoleRequest request) {
        log.info("Creating role in service: {}", request);
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());

        if (permissions.isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_FOUND); // hoặc tạo ErrorCode phù hợp
        }

        role.setPermissions(new HashSet<>(permissions));

        log.info("Creating role: {}", request);
        log.info("Found permissions: {}", permissions);

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }
    
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }
    
    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
