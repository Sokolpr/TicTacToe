package s21.peanutsh.TicTacToe.domain.service;

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
import s21.peanutsh.TicTacToe.web.mapper.PersonMapper;
import s21.peanutsh.TicTacToe.web.model.*;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceAuth {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;



    public void registration(SignUpRequest signUpRequest) throws AuthorizationDeniedException {
        userService.signUp(signUpRequest);
    }

    public JWTResponse updateAccessToken(String refresh){
        return updateToken(refresh,"access");
    }

    public JWTResponse updateRefreshToken(String refresh){
        return updateToken(refresh,"refresh");
    }



    public JWTResponse authorize(JWTRequest jwtRequest) throws Exception {
        var user = userService.loadUserByUsername(jwtRequest.getLogin());
        if (passwordEncoder.matches(jwtRequest.getPassword(), user.getPassword())){
            var access= jwtProvider.generationAccessToken(user);
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
        }else {
            throw new Exception("invalid password");
        }
    }

    public WebPerson getUser(UUID uuidUser) throws UsernameNotFoundException {
        var user = userService.getByUuid(uuidUser);
        return PersonMapper.entityToWeb(user.getLogin(), uuidUser);
    }

    public JwtAuthentication getAuth(){
       return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }


    private JWTResponse updateToken(@NonNull String token, String type){
        var copyToken = token;
        if(jwtProvider.validateAccessToken(copyToken)){
            var claims = jwtProvider.getRefreshClaims(copyToken);
            var user= userService.getByUuid((UUID)claims.get("uuid"));
            var updateAccess= jwtProvider.generationAccessToken(user);
            if (type.equals("refresh")){
                copyToken = jwtProvider.generationRefreshToken(user);
                tokenRepository.save(TokenEntity.builder()
                        .uuid(user.getUuid())
                        .token(copyToken)
                        .build());
            }
            return JWTResponse.builder()
                    .refreshToken(copyToken)
                    .accessToken(updateAccess)
                    .build();
        }else {
            throw  new JwtException("Invalid Jwt token");
        }
    }


}
