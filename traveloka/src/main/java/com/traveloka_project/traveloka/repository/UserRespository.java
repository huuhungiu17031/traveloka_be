package com.traveloka_project.traveloka.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traveloka_project.traveloka.model.User;
import java.util.Optional;

public interface UserRespository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
