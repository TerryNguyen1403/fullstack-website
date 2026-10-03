package com.example.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.server.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
