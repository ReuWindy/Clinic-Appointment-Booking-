package fsa.grp4.clinic_appointment.security.jwt;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import fsa.grp4.clinic_appointment.entity.User;

@Component
public class JwtTokenManager {
    private final JwtProperties jwtProperties;

    public JwtTokenManager(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(User user) {

        String role = user.getRole().name().startsWith("ROLE_")
                ? user.getRole().name()
                : "ROLE_" + user.getRole().name();

        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer(jwtProperties.getIssuer())
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getAccessTokenExpiration()))
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes()));
    }

    public String getUsernameFromToken(String token) {
        return getDecodedJWT(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        String role = getDecodedJWT(token).getClaim("role").asString();
        return role == null ? List.of() : List.of(role);
    }

    public String resolveTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            getDecodedJWT(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    private DecodedJWT getDecodedJWT(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes()))
                .withIssuer(jwtProperties.getIssuer())
                .build();

        return verifier.verify(token);
    }
}
