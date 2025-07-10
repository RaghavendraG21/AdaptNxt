//package com.ecommerce.api.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//
//@Component
//public class JwtTokenProvider {
//
//    private String secret = "mySecretKeyForJWTGeneration123!"; // Use a strong secret
//    private Key secretKey;
//
//    @PostConstruct
//    protected void init() {
//        byte[] secretBytes = Base64.getEncoder().encode(secret.getBytes());
//        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
//    }
//
//    public String generateToken(String username) {
//        Date now = new Date();
//        Date expiry = new Date(now.getTime() + 86400000); // 1 day
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(now)
//                .setExpiration(expiry)
//                .signWith(secretKey, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(secretKey)
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            System.out.println("Invalid JWT: " + e.getMessage());
//            return false;
//        }
//    }
//}



package com.ecommerce.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private String secret = "mySecretKeyForJWTGeneration123!"; // Use a strong secret
    private Key secretKey;

    @PostConstruct
    protected void init() {
        byte[] secretBytes = Base64.getEncoder().encode(secret.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
    }

    // ✅ Generate token with roles
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + 86400000); // 1 day

        List<String> roles = userDetails.getAuthorities()
                                        .stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles) // ✅ include roles
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Extract roles from token
    public List<GrantedAuthority> getRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> role)
                .collect(Collectors.toList());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }
}

