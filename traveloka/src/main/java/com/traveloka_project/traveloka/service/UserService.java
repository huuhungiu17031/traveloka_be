package com.traveloka_project.traveloka.service;

import com.traveloka_project.traveloka.model.User;
import com.traveloka_project.traveloka.payload.response.JwtResponse;
import com.traveloka_project.traveloka.payload.response.UserInfor;
import com.traveloka_project.traveloka.payload.resquest.CreateUserRequest;

public interface UserService {
    User findUserByEmail(String email);

    JwtResponse login(String email, String password);

    CreateUserRequest register(CreateUserRequest createUserRequest);

    UserInfor getUserInfor();

}
