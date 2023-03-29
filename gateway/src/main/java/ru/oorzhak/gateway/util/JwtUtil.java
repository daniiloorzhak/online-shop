package ru.oorzhak.gateway.util;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class JwtUtil {
    @Value("${jwt.key.public}")
    private String jwtPublicKey;
    @Value("${jwt.expiration.refresh.days}")
    private long jwtExpirationRefreshDays;
    @Value("${jwt.expiration.access.minutes}")
    private long jwtExpirationAccessMinutes;

    public String getUsernameFromJwtToken(String jwt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] encodedPv = Base64.getDecoder().decode(jwtPublicKey.getBytes(StandardCharsets.US_ASCII));
        X509EncodedKeySpec keySpecPv = new X509EncodedKeySpec(encodedPv);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        PublicKey publicKey = kf.generatePublic(keySpecPv);

        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }

    public boolean isJwtValid(String jwt) {
        try {
            byte[] encodedPublic = Base64.getDecoder().decode(jwtPublicKey.getBytes(StandardCharsets.US_ASCII));
            var keySpecPv = new X509EncodedKeySpec(encodedPublic);
            var kf = KeyFactory.getInstance("RSA");

            PublicKey publicKey = kf.generatePublic(keySpecPv);

            Jwts.parserBuilder().setSigningKey(publicKey).build().parse(jwt);
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
