package com.traveloka_project.traveloka.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traveloka_project.traveloka.model.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByUser_Id(Integer userId);

    Optional<RefreshToken> findByrefreshToken(String token);

    Optional<RefreshToken> findByUser_Email(String email);

}
