package s21.peanutsh.TicTacToe.domain.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s21.peanutsh.TicTacToe.datasource.model.TokenEntity;
import s21.peanutsh.TicTacToe.datasource.repository.TokenRepository;
import s21.peanutsh.TicTacToe.domain.service.impl.JwtProviderImpl;
import s21.peanutsh.TicTacToe.domain.service.impl.UserServiceImpl;
import s21.peanutsh.TicTacToe.web.mapper.PersonMapper;
import s21.peanutsh.TicTacToe.web.model.*;

import java.util.UUID;

@Service
public interface AuthService {


    public void registration(SignUpRequest signUpRequest) throws AuthorizationDeniedException;

    public JWTResponse updateAccessToken(String refresh) throws Exception ;

    public JWTResponse updateRefreshToken(String refresh) throws Exception ;


    public JWTResponse login(JWTRequest jwtRequest) throws Exception ;

    public WebPerson getUserByUuid(UUID uuidUser) throws UsernameNotFoundException ;

    public JwtAuthentication getAuth() ;


    public WebPerson getUserByAccessToken(@NonNull String token) throws UsernameNotFoundException, JwtException ;


}
