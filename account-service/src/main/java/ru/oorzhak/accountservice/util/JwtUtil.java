package ru.oorzhak.accountservice.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.key.private}")
    private String jwtPrivateKey;
    @Value("${jwt.key.public}")
    private String jwtPublicKey;
    @Value("${jwt.expiration.refresh.days}")
    private long jwtExpirationRefreshDays;
    @Value("${jwt.expiration.access.minutes}")
    private long jwtExpirationAccessMinutes;

    public PublicKey getPublicKey() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            byte[] encodedPublic = Base64.getDecoder().decode(jwtPublicKey.getBytes(StandardCharsets.US_ASCII));
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(encodedPublic);
            return kf.generatePublic(keySpecPublic);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey getPrivateKey() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            byte[] encodedPrivate = Base64.getDecoder().decode(jwtPrivateKey.getBytes(StandardCharsets.US_ASCII));
            PKCS8EncodedKeySpec keySpecPrivate = new PKCS8EncodedKeySpec(encodedPrivate);
            return  kf.generatePrivate(keySpecPrivate);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

    }


    public String generateRefreshToken(Authentication authentication) {
        try {
            return Jwts.builder()
                .setClaims(Map.of("admin", isAdmin(authentication)))
                .setIssuer("daniil")
                .setSubject(authentication.getName())
                .setExpiration(Date.from(ZonedDateTime.now().plusDays(jwtExpirationRefreshDays).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey())
                .compact();
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public String generateAccessToken(Authentication authentication) {
        try {
            return Jwts.builder()
                    .setClaims(Map.of("admin", isAdmin(authentication)))
                    .setIssuer("daniil")
                    .setSubject(authentication.getName())
                    .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(jwtExpirationAccessMinutes).toInstant()))
                    .setIssuedAt(new Date())
                    .signWith(getPrivateKey())
                    .compact();
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsernameFromJwtToken(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isJwtValid(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(getPublicKey()).build().parse(jwt);
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }
}
