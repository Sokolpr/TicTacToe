package s21.peanutsh.TicTacToe.domain.service.impl;

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
import s21.peanutsh.TicTacToe.domain.service.AuthService;
import s21.peanutsh.TicTacToe.web.mapper.PersonMapper;
import s21.peanutsh.TicTacToe.web.model.*;

import java.util.UUID;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserServiceImpl userServiceImpl;
    private final JwtProviderImpl jwtProviderImpl;
    private final TokenRepository tokenRepository;



    @Override
    public void registration(SignUpRequest signUpRequest) throws AuthorizationDeniedException {
        userServiceImpl.signUp(signUpRequest);
    }

    @Override
    public JWTResponse updateAccessToken(String refresh) throws Exception {
        return updateToken(refresh,"access");
    }

    @Override
    public JWTResponse updateRefreshToken(String refresh) throws Exception {
        return updateToken(refresh,"refresh");
    }


    @Override
    public JWTResponse login(JWTRequest jwtRequest) throws Exception {
        var user = userServiceImpl.loadUserByUsername(jwtRequest.getLogin());
        if (passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword())){
            var access= jwtProviderImpl.generationAccessToken(user);
            var refresh = jwtProviderImpl.generationRefreshToken(user);
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
        }else {
            throw new Exception("invalid password");
        }
    }

    @Override
    public WebPerson getUserByUuid(UUID uuidUser) throws UsernameNotFoundException {
        var user = userServiceImpl.getByUuid(uuidUser);
        return PersonMapper.entityToWeb(user.getLogin(), uuidUser);
    }

    @Override
    public JwtAuthentication getAuth(){
       return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }


    private JWTResponse updateToken(String refreshToken,String typeToken)throws Exception {
        var refresh  = refreshToken;
        if (jwtProviderImpl.validateRefreshToken(refresh)) {
            Claims claims = jwtProviderImpl.getRefreshClaims(refresh);
            UUID uuidUser = (UUID) claims.get("uuid");
            String storeToken = tokenRepository.findByUuid(uuidUser).orElseThrow(() -> new Exception("пользователь не найден")).getToken();
            if (refresh.equals(storeToken)) {
                var user=  userServiceImpl.getByUuid(uuidUser);
                String updateAccessToken = jwtProviderImpl.generationAccessToken(user);
                if (typeToken.equals("refresh")){
                    refresh = jwtProviderImpl.generationRefreshToken(user);
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
    public WebPerson getUserByAccessToken(@NonNull String token) throws UsernameNotFoundException , JwtException {
        if (jwtProviderImpl.validateAccessToken(token)){
            var claims = jwtProviderImpl.getAccessClaims(token);
            var user = userServiceImpl.getByUuid((UUID) claims.get("uuid"));
            return PersonMapper.entityToWeb(user.getLogin(),user.getUuid());
        }
        else {
            throw  new JwtException("invalid token");
        }

    }


}
