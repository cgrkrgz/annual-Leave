package com.annualLeave.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyString;

    private Key key;

    @PostConstruct
    public void init() {
        // Base64 decode ederek Key objesine çeviriyoruz
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    // Token üret
    public String generateToken(String tc, String userType) {
        return Jwts.builder()
                .setSubject(tc)
                .claim("userType", userType)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 saat geçerli
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // TC'yi token'dan çıkar
    public String extractTc(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Kullanıcı rolünü token'dan çıkar
    public String extractUserType(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userType", String.class);
    }

    // Token geçerlilik kontrolü
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("JWT doğrulama hatası: " + e.getMessage());
            return false;
        }
    }
}
