package com.traveloka_project.traveloka.provider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.exception.NotFoundException;
import com.traveloka_project.traveloka.model.Role;
import com.traveloka_project.traveloka.model.User;
import com.traveloka_project.traveloka.service.UserService;

@Service
public class CustomAuthenProvider implements AuthenticationProvider {
    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.findUserByEmail(email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            List<GrantedAuthority> listRoles = new ArrayList<>();
            for (Role role : user.getRoles()) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
                listRoles.add(authority);
            }
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    listRoles);
            return token;
        }
        throw new NotFoundException("ErrorMessage.INVALID_ACCOUNT");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean valid = authentication.equals(UsernamePasswordAuthenticationToken.class);
        if (valid)
            return true;
        throw new UnsupportedOperationException("Unimplemented method 'supports'");
    }
}
