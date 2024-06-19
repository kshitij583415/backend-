package com.qualityEducation.backend.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.cookieName}")
    private String cookieName;

    public String generateToken(Long userId, List<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", roles);
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public void addTokenToCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setPath("/");
        // cookie.setHttpOnly(true); // Prevents JavaScript access to the cookie
        cookie.setMaxAge(expiration); // Cookie expiration in seconds
        response.addCookie(cookie);
    }

    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return (Long) claims.get("userId");
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return (List<String>) claims.get("roles");
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public void expireUserCookie(HttpServletResponse response) {
        // Create an expired user cookie
        Cookie cookie = new Cookie("user", null);
        cookie.setMaxAge(0); // Expires immediately
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}