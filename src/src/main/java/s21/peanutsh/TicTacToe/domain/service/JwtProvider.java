package s21.peanutsh.TicTacToe.domain.service;


import io.jsonwebtoken.*;
import lombok.NonNull;
import s21.peanutsh.TicTacToe.domain.model.User;

public interface JwtProvider {


     String generationAccessToken(@NonNull User user);

     String generationRefreshToken(@NonNull User user);

     boolean validateAccessToken(@NonNull String token);

     boolean validateRefreshToken(@NonNull String token);


     Claims getAccessClaims(@NonNull String token);

     Claims getRefreshClaims(@NonNull String token);


}
