package com.traveloka_project.traveloka.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.exception.NotFoundException;
import com.traveloka_project.traveloka.model.Role;
import com.traveloka_project.traveloka.model.User;
import com.traveloka_project.traveloka.payload.response.JwtResponse;
import com.traveloka_project.traveloka.payload.response.UserInfor;
import com.traveloka_project.traveloka.payload.resquest.CreateUserRequest;
import com.traveloka_project.traveloka.repository.UserRespository;
import com.traveloka_project.traveloka.service.JwtService;
import com.traveloka_project.traveloka.service.RoleService;
import com.traveloka_project.traveloka.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRespository userRespository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RoleService roleService;

    @Override
    public User findUserByEmail(String email) {
        Optional<User> optionalUser = userRespository.findByEmail(email);
        if (optionalUser.isEmpty())
            throw new NotFoundException("Email not founded!");
        return optionalUser.get();
    }

    @Override
    public JwtResponse login(String email, String password) {
        UsernamePasswordAuthenticationToken authen = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authen);
        List<String> listRoles = authentication.getAuthorities().stream().map((authority) -> authority.getAuthority())
                .collect(Collectors.toList());
        JwtResponse jwtResponse = jwtService.generateJwtResponse(email, listRoles);
        return jwtResponse;
    }

    @Override
    public CreateUserRequest register(CreateUserRequest createUserRequest) {
        String encryptedPassword = passwordEncoder.encode(createUserRequest.getPassword());
        createUserRequest.setPassword(encryptedPassword);
        Role role = roleService.findByRole("ROLE_USER");
        User newUser = createUserRequest.dto();
        List<Role> listRoles = new ArrayList<>();
        listRoles.add(role);
        newUser.setRoles(listRoles);
        return createUserRequest.otd(userRespository.save(newUser));
    }

    @Override
    public UserInfor getUserInfor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        List<String> listRoles = authentication.getAuthorities().stream().map((authority) -> authority.getAuthority())
                .collect(Collectors.toList());
        UserInfor userInfor = new UserInfor();
        userInfor.setEmail(email);
        userInfor.setRoles(listRoles);
        return userInfor;
    }
}
