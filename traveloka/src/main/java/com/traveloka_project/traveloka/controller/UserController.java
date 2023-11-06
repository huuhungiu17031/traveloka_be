package com.traveloka_project.traveloka.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traveloka_project.traveloka.payload.response.BaseHttpResponse;
import com.traveloka_project.traveloka.payload.response.JwtResponse;
import com.traveloka_project.traveloka.payload.response.UserInfor;
import com.traveloka_project.traveloka.payload.resquest.CreateUserRequest;
import com.traveloka_project.traveloka.payload.resquest.UserRequest;
import com.traveloka_project.traveloka.service.JwtService;
import com.traveloka_project.traveloka.service.RefreshTokenService;
import com.traveloka_project.traveloka.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    
    public UserController(UserService userService, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("login")
    public ResponseEntity<BaseHttpResponse> login(@RequestBody UserRequest userRequest) {
        JwtResponse jwtResponse = userService.login(userRequest.getEmail(), userRequest.getPassword());
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), jwtResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("register")
    public ResponseEntity<BaseHttpResponse> register(@RequestBody CreateUserRequest createUserRequest) {
        userService.register(createUserRequest);
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), "Register successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("refreshToken")
    public ResponseEntity<BaseHttpResponse> getRefreshToken(@RequestBody String refreshToken) {
        JwtResponse jwtResponse = jwtService.getNewJwtToken(refreshToken);
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), jwtResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("info")
    public ResponseEntity<BaseHttpResponse> getUserInfor() {
        UserInfor userInfor = userService.getUserInfor();
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), userInfor);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("provoke")
    public ResponseEntity<BaseHttpResponse> provokeToken(@RequestBody String email) {
        refreshTokenService.provokeToken(email);
        BaseHttpResponse response = new BaseHttpResponse(HttpStatus.OK.value(), "Provoke successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
