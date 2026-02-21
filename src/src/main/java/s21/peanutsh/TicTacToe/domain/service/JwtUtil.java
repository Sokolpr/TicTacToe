package s21.peanutsh.TicTacToe.domain.service;


import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import s21.peanutsh.TicTacToe.domain.model.Role;
import s21.peanutsh.TicTacToe.web.model.JwtAuthentication;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    public  JwtAuthentication generate(Claims claims){
        return JwtAuthentication.builder()
                .uuid((UUID)claims.get("uuid"))
                .roles(getRoles(claims))
                .build();


    }
    private static Set<Role> getRoles (Claims claims){
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

}
