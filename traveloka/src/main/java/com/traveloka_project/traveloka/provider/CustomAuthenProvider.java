package com.traveloka_project.traveloka.provider;

import java.util.ArrayList;
import java.util.List;

import com.traveloka_project.traveloka.exception.CommonException;
import com.traveloka_project.traveloka.util.ErrorMessage;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.model.Role;
import com.traveloka_project.traveloka.model.User;
import com.traveloka_project.traveloka.service.UserService;

@Service
public class CustomAuthenProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenProvider(@Lazy UserService userService, @Lazy PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

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
        throw new CommonException(ErrorMessage.EMAIL_OR_PASSWORD_ERROR);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        boolean valid = authentication.equals(UsernamePasswordAuthenticationToken.class);
        if (valid)
            return true;
        throw new UnsupportedOperationException("Unimplemented method 'supports'");
    }
}
