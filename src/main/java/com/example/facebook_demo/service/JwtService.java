package com.example.facebook_demo.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private String secretKey;
    public JwtService() throws NoSuchAlgorithmException{
        try{
            KeyGenerator keyGen=KeyGenerator.getInstance(("HmacSHA256"));
            SecretKey sk=keyGen.generateKey();
            secretKey=Base64.getEncoder().encodeToString(sk.getEncoded());
        }
        catch(NoSuchAlgorithmException ex){
            throw new RuntimeException(ex);
        }
    }

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        
        return Jwts.builder()
            .claim("type", "access")
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) //15 min
            .signWith(getKey())
            .compact();
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
            .claim("type", "refresh")
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7)) // 7 days
            .signWith(getKey())
            .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims=extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
    return Jwts.parser()
            .setSigningKey(getKey())
            .build()
            .parseSignedClaims(token).getPayload();

    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName=extractUserName(token);
        return (userName.equals(userDetails.getUsername())&&!isTokenExpired(token));
    }
    public boolean validateRefreshToken(String token){
        try{
            Claims claims=extractAllClaims(token);
            Date expiration=claims.getExpiration();
            return expiration.after(new Date()); 
        }
        catch(Exception ex){
            return false;
        }
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractTokenType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }

    public String generatePasswordResetToken(String email,int minutes) {
    return Jwts.builder()
        .claim("type", "password_reset")
        .setSubject(email)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15)) // 15 min expiry
        .signWith(getKey())
        .compact();
    }

}
