package com.traveloka_project.traveloka.service;

import com.traveloka_project.traveloka.model.RefreshToken;
import com.traveloka_project.traveloka.model.User;

public interface RefreshTokenService {

    RefreshToken save(String token, User user);

    RefreshToken processRefreshToken(String email);

    RefreshToken handleGetNewRefreshToken(String token);

    void provokeToken(String token);
    
}
