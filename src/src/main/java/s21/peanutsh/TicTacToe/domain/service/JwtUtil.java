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
public interface JwtUtil {
    public  JwtAuthentication generate(Claims claims);
}
