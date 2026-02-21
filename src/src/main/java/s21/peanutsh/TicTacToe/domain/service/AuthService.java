package s21.peanutsh.TicTacToe.domain.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s21.peanutsh.TicTacToe.datasource.model.TokenEntity;
import s21.peanutsh.TicTacToe.datasource.repository.TokenRepository;
import s21.peanutsh.TicTacToe.web.model.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;



    public void registration(SignUpRequest signUpRequest) throws AuthorizationDeniedException {
        userService.signUp(signUpRequest);
    }

    public JWTResponse login(JWTRequest jwtRequest) throws Exception {
        var user = userService.loadUserByUsername(jwtRequest.getLogin());
        if (passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword())) {
            final String refreshToken = jwtProvider.generationRefreshToken(user);
            final String accessToken = jwtProvider.generationAccessToken(user);
            tokenRepository.save(TokenEntity.builder().token(refreshToken).uuid(user.getUuid()).build());
            return JWTResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new Exception("Неверный пароль");
        }
    }


    public JWTResponse updateAccessToken(String refreshToken) throws Exception {
        return updateToken(refreshToken,"access");
    }

    public JWTResponse updateRefreshToken(String refreshToken) throws Exception {
        return updateToken(refreshToken,"refresh");
    }


    private JWTResponse updateToken(String refreshToken,String typeToken)throws Exception {
        var refresh  = refreshToken;
        if (jwtProvider.validateRefreshToken(refresh)) {
            Claims claims = jwtProvider.getRefreshClaims(refresh);
            UUID uuidUser = (UUID) claims.get("uuid");
            String storeToken = tokenRepository.findByUuid(uuidUser).orElseThrow(() -> new Exception("пользователь не найден")).getToken();
            if (refresh.equals(storeToken)) {
                var user=  userService.getByUuid(uuidUser);
                String updateAccessToken = jwtProvider.generationAccessToken(user);
                if (typeToken.equals("refresh")){
                    refresh = jwtProvider.generationRefreshToken(user);
                    tokenRepository.save(TokenEntity.builder().token(refresh).uuid(user.getUuid()).build());
                }
                return JWTResponse.builder()
                        .accessToken(updateAccessToken)
                        .refreshToken(refresh)
                        .build();
            }
        }
        throw new Exception("не валидный jwt");
    }


    public JwtAuthentication getAuthentication(){
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }





}
