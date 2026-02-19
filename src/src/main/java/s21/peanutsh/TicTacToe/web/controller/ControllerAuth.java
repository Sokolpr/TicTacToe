package s21.peanutsh.TicTacToe.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import s21.peanutsh.TicTacToe.domain.service.ServiceAuth;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;

import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class ControllerAuth {

    private static final Logger logger = Logger.getLogger(ControllerAuth.class.getName());


    private final ServiceAuth serviceAuth;

    public ControllerAuth(ServiceAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }

    //регистрация
    @PostMapping("/auth/signup")
    ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        logger.info("Отправлен запрос на регистрацию");
        try {
            serviceAuth.registration(signUpRequest);
            return ResponseEntity.ok("Пользователь зарегистрирован");

        } catch (AuthorizationDeniedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Такой пользователь уже существует");
        }
    }


    //авторизация
    @PostMapping("/auth/login")
    ResponseEntity<?> login(@RequestHeader("Authorization") String authHeader) {
        logger.info("Отправлен запрос на авторизацию");
        try {
            var auth = serviceAuth.authorize(authHeader);
            return ResponseEntity.ok(auth);

        } catch (Exception ignored) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Неверный логин или пароль");

        }
    }

    ;

    @PostMapping("/user/{uuidUser}")
    ResponseEntity<String> getUser(
            @PathVariable UUID uuidUser
    ) {
        try {
            var user = serviceAuth.getUser(uuidUser);
            return ResponseEntity.ok(user.toString());
        } catch (UsernameNotFoundException ignored) {
            return ResponseEntity.ok("Пользователь не найден");
        }
    }


}