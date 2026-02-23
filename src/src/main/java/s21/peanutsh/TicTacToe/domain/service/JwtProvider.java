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

public interface JwtProvider {


    public String generationAccessToken(@NonNull User user);

    public String generationRefreshToken(@NonNull User user);

    public boolean validateAccessToken(@NonNull String token);

    public boolean validateRefreshToken(@NonNull String token);


    public Claims getAccessClaims(@NonNull String token);

    public Claims getRefreshClaims(@NonNull String token);


}
