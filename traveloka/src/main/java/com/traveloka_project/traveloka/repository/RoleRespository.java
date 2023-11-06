package com.traveloka_project.traveloka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traveloka_project.traveloka.model.Role;

public interface RoleRespository extends JpaRepository<Role, Integer> {
    Optional<Role> findByRole(String role);
}
