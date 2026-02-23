package s21.peanutsh.TicTacToe.domain.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import s21.peanutsh.TicTacToe.datasource.model.TokenEntity;
import s21.peanutsh.TicTacToe.datasource.repository.TokenRepository;
import s21.peanutsh.TicTacToe.domain.service.AuthService;
import s21.peanutsh.TicTacToe.domain.service.JwtProvider;
import s21.peanutsh.TicTacToe.domain.service.UserService;
import s21.peanutsh.TicTacToe.web.mapper.PersonMapper;
import s21.peanutsh.TicTacToe.web.model.*;

import java.util.UUID;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void registration(SignUpRequest signUpRequest) throws AuthorizationDeniedException {
        userService.signUp(signUpRequest);
    }

    @Override
    public JWTResponse updateAccessToken(String refresh) throws Exception {
        return updateToken(refresh, "access");
    }

    @Override
    public JWTResponse updateRefreshToken(String refresh) throws Exception {
        return updateToken(refresh, "refresh");
    }


    @Override
    public JWTResponse login(JWTRequest jwtRequest) throws Exception {
        var user = userService.loadUserByUsername(jwtRequest.getLogin());
        if (passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword())) {
            var access = jwtProvider.generationAccessToken(user);
            var refresh = jwtProvider.generationRefreshToken(user);
            tokenRepository.save(
                    TokenEntity.builder()
                            .token(refresh)
                            .uuid(user.getUuid())
                            .build()
            );
            return JWTResponse
                    .builder()
                    .accessToken(access)
                    .refreshToken(refresh)
                    .build();
        } else {
            throw new Exception("invalid password");
        }
    }

    @Override
    public WebPerson getUserByUuid(UUID uuidUser) throws UsernameNotFoundException {
        var user = userService.getByUuid(uuidUser);
        return PersonMapper.entityToWeb(user.getLogin(), uuidUser);
    }

    @Override
    public JwtAuthentication getAuth() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }


    private JWTResponse updateToken(String refreshToken, String typeToken) throws Exception {
        var refresh = refreshToken;
        if (jwtProvider.validateRefreshToken(refresh)) {
            Claims claims = jwtProvider.getRefreshClaims(refresh);
            UUID uuidUser = (UUID) claims.get("uuid");
            String storeToken = tokenRepository.findByUuid(uuidUser).orElseThrow(() -> new Exception("пользователь не найден")).getToken();
            if (refresh.equals(storeToken)) {
                var user = userService.getByUuid(uuidUser);
                String updateAccessToken = jwtProvider.generationAccessToken(user);
                if (typeToken.equals("refresh")) {
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


    @Override
    public WebPerson getUserByAccessToken(@NonNull String token) throws UsernameNotFoundException, JwtException {
        if (jwtProvider.validateAccessToken(token)) {
            var claims = jwtProvider.getAccessClaims(token);
            var user = userService.getByUuid((UUID) claims.get("uuid"));
            return PersonMapper.entityToWeb(user.getLogin(), user.getUuid());
        } else {
            throw new JwtException("invalid token");
        }

    }


}
