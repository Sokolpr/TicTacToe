package s21.peanutsh.TicTacToe.domain.service.impl;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import s21.peanutsh.TicTacToe.domain.model.User;
import s21.peanutsh.TicTacToe.domain.service.JwtProvider;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class JwtProviderImpl implements JwtProvider {

    private final SecretKey accessSecret;
    private final Integer lifeTimeAccess;
    private final Integer lifeTimeRefresh;
    private final SecretKey refreshSecret;


    public JwtProviderImpl(String accessSecret,
                           Integer lifeTimeAccess,
                           String refreshSecret,
                           Integer lifeTimeRefresh) {
        this.accessSecret = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecret = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.lifeTimeAccess = lifeTimeAccess;
        this.lifeTimeRefresh = lifeTimeRefresh;
    }

    @Override
    public String generationAccessToken(@NonNull User user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .claim("uuid", user.getUuid())
                .claim("roles", user.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifeTimeAccess))
                .signWith(accessSecret)
                .compact();

    }

    @Override
    public String generationRefreshToken(@NonNull User user) {
        return Jwts.builder()
                .subject(user.getLogin())
                .claim("uuid", user.getUuid())
                .claim("roles", user.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + lifeTimeRefresh))
                .signWith(refreshSecret)
                .compact();
    }

    @Override
    public boolean validateAccessToken(@NonNull String token) {
        return validateToken(token, accessSecret);
    }

    @Override
    public boolean validateRefreshToken(@NonNull String token) {
        return validateToken(token, refreshSecret);
    }


    private boolean validateToken(@NonNull String token, @NonNull SecretKey secret) throws ExpiredJwtException {
        try {
            Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt", unsEx);
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt", mjEx);
        } catch (SignatureException sEx) {
            log.error("Invalid signature", sEx);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    @Override
    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, accessSecret);
    }

    @Override
    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, refreshSecret);
    }

    private Claims getClaims(String token, SecretKey secretKey) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
