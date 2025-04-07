package com.example.hocspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hocspring.entity.Permission;

@Repository
public interface PermissionRepository  extends JpaRepository<Permission, String> {
    // Optional<Permission> findByName(String name);
    // boolean existsByName(String name);
    // List<Permission> findAllByNameIn(List<String> names);

}
