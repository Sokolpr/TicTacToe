package s21.peanutsh.TicTacToe.domain.service;

import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import s21.peanutsh.TicTacToe.web.model.*;

import java.util.UUID;


public interface AuthService {


    void registration(SignUpRequest signUpRequest) throws AuthorizationDeniedException;

    JWTResponse updateAccessToken(String refresh) throws Exception;

    JWTResponse updateRefreshToken(String refresh) throws Exception;


    JWTResponse login(JWTRequest jwtRequest) throws Exception;

    WebPerson getUserByUuid(UUID uuidUser) throws UsernameNotFoundException;

    JwtAuthentication getAuth();


    WebPerson getUserByAccessToken(@NonNull String token) throws UsernameNotFoundException, JwtException;


}
