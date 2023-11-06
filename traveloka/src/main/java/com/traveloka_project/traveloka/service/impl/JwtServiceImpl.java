package com.traveloka_project.traveloka.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.model.RefreshToken;
import com.traveloka_project.traveloka.model.User;
import com.traveloka_project.traveloka.payload.response.JwtResponse;
import com.traveloka_project.traveloka.service.JwtService;
import com.traveloka_project.traveloka.service.RefreshTokenService;
import com.traveloka_project.traveloka.util.Constant;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;

@Service
public class JwtServiceImpl implements JwtService {
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Value("${token.secrect.keys}")
    private String JWT_SECRET;
    private final int accessTokenDuration = 48 * 60 * 60 * 1000;

    private Key getSigningKey() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET));
        return key;
    }

    @Override
    public String generateToken(String email, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenDuration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    @Override
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    @Override
    public Boolean isTokenValid(String token, String email) {
        final String emailInsideToken = extractEmail(token);
        if (emailInsideToken.equals(email) && !extractExpiration(token).before(new Date())) {
            return true;
        }
        return false;
    }

    @Override
    public JwtResponse getNewJwtToken(String token) {
        RefreshToken refreshToken = refreshTokenService.handleGetNewRefreshToken(token);
        User user = refreshToken.getUser();
        List<String> listRoles = user.getRoles().stream().map((role) -> role.getRole()).collect(Collectors.toList());
        return generateJwtResponse(user.getEmail(), listRoles);
    }

    @Override
    public JwtResponse generateJwtResponse(String email, List<String> roles) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put(Constant.LIST_ROLE_KEY, roles);
        String accessToken = generateToken(email, extraClaims);
        RefreshToken refreshToken = refreshTokenService.processRefreshToken(email);
        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getRefreshToken())
                .build();
        return jwtResponse;
    }
}