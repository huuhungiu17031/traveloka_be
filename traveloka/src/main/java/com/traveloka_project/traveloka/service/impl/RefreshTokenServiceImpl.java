package com.traveloka_project.traveloka.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.traveloka_project.traveloka.exception.CommonException;
import com.traveloka_project.traveloka.exception.NotFoundException;
import com.traveloka_project.traveloka.model.RefreshToken;
import com.traveloka_project.traveloka.model.User;
import com.traveloka_project.traveloka.repository.RefreshTokenRepository;
import com.traveloka_project.traveloka.repository.UserRespository;
import com.traveloka_project.traveloka.service.RefreshTokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRespository userRespository;
    private final int refreshTokenDuration = 48 * 60 * 60 * 1000;
    @Value("${token.secrect.refreshKey}")
    private String JWT_REFRESH_TOKEN;

    private String generateRefreshToken(String email) {
        Map<String, Object> extraClaims = new HashMap<>();
        UUID uuid = UUID.randomUUID();
        String randomString = Long.toString(System.currentTimeMillis()) + uuid.toString();
        extraClaims.put("randomString", randomString);
        return Jwts.builder()
                .setSubject(email)
                .setClaims(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenDuration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenValid(String token) {
        if (!extractExpiration(token).before(new Date())) {
            return true;
        }
        return false;
    }

    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration();
    }

    private Key getSigningKey() {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_REFRESH_TOKEN));
        return key;
    }

    private String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    @Override
    public RefreshToken save(String token, User user) {
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setRefreshToken(token);
        newRefreshToken.setUser(user);
        return refreshTokenRepository.save(newRefreshToken);
    }

    @Override
    public RefreshToken processRefreshToken(String email) {
        Optional<User> optional = userRespository.findByEmail(email);
        User user = optional.get();
        if (user.getRefreshToken() == null) {
            String refreshToken = this.generateRefreshToken(email);
            RefreshToken newRefreshToken = new RefreshToken();
            newRefreshToken.setRefreshToken(refreshToken);
            newRefreshToken.setUser(user);
            return refreshTokenRepository.save(newRefreshToken);
        }
        RefreshToken availableRefreshToken = user.getRefreshToken();
        availableRefreshToken.setRefreshToken(generateRefreshToken(email));
        return refreshTokenRepository.save(availableRefreshToken);
    }

    @Override
    public RefreshToken handleGetNewRefreshToken(String token) {
        Optional<RefreshToken> optional = refreshTokenRepository.findByrefreshToken(token);
        if (optional.isEmpty())
            throw new NotFoundException("Not Found Token");
        RefreshToken dbRefreshToken = optional.get();
        if (isTokenValid(dbRefreshToken.getRefreshToken())) {
            String email = extractEmail(token);
            String newToken = generateRefreshToken(email);
            dbRefreshToken.setRefreshToken(newToken);
            return refreshTokenRepository.save(dbRefreshToken);
        }
        refreshTokenRepository.deleteById(dbRefreshToken.getId());
        throw new CommonException("Sign in again");
    }

    @Override
    public void provokeToken(String email) {
        Optional<RefreshToken> optional = refreshTokenRepository.findByUser_Email(email);
        if (optional.isEmpty())
            throw new NotFoundException("Not Found Provoke Token");
        RefreshToken dbRefreshToken = optional.get();
        refreshTokenRepository.deleteById(dbRefreshToken.getId());
    }
}
