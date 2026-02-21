package s21.peanutsh.TicTacToe.domain.service;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import s21.peanutsh.TicTacToe.domain.model.User;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Slf4j
@Component
public class JwtProvider {

    private final SecretKey accessSecret;
    private final Integer lifeTimeAccess;
    private final Integer lifeTimeRefresh;
    private final SecretKey refreshSecret;

    public JwtProvider(@Value("${secretAccess}") String accessSecret,
                       @Value("${lifeTimeAccess}") Integer lifeTimeAccess,
                       @Value("${secretRefresh}") String refreshSecret,
                       @Value("${lifeTimeRefresh}") Integer lifeTimeRefresh) {
        this.accessSecret = Keys.hmacShaKeyFor(accessSecret.getBytes(StandardCharsets.UTF_8));
        this.refreshSecret = Keys.hmacShaKeyFor(refreshSecret.getBytes(StandardCharsets.UTF_8));
        this.lifeTimeAccess = lifeTimeAccess;
        this.lifeTimeRefresh = lifeTimeRefresh;
    }

    public String generationAccessToken(@NonNull User user){
        return  Jwts.builder()
                .subject(user.getLogin())
                .claim("uuid", user.getUuid())
                .claim("roles",user.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() +lifeTimeAccess))
                .signWith(accessSecret)
                .compact();

    }

    public String generationRefreshToken(@NonNull User user){
        return Jwts.builder()
                .subject(user.getLogin())
                .claim("uuid", user.getUuid())
                .claim("roles",user.getRoles())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() +lifeTimeRefresh))
                .signWith(refreshSecret)
                .compact();
    }


    public boolean validateAccessToken( @NonNull String token){
        return validateToken(token,accessSecret);
    }

    public boolean validateRefreshToken( @NonNull String token){
        return validateToken(token,refreshSecret);
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

    public Claims getAccessClaims(@NonNull String token){
        return getClaims(token,accessSecret);
    }

    public Claims getRefreshClaims(@NonNull String token){
        return getClaims(token,refreshSecret);
    }

    private Claims getClaims(String token, SecretKey secretKey){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



}
