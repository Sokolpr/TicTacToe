package s21.peanutsh.TicTacToe.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import s21.peanutsh.TicTacToe.domain.service.ServiceAuth;
import s21.peanutsh.TicTacToe.web.model.JWTRequest;
import s21.peanutsh.TicTacToe.web.model.RefreshJwtRequest;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;

import java.util.UUID;
import java.util.logging.Logger;
@Slf4j
@RestController
public class ControllerAuth {



    private final ServiceAuth serviceAuth;

    public ControllerAuth(ServiceAuth serviceAuth) {
        this.serviceAuth = serviceAuth;
    }

    //регистрация
    @PostMapping("/auth/signup")
    ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        log.info("Отправлен запрос на регистрацию");
        try {
            serviceAuth.registration(signUpRequest);
            return ResponseEntity.ok("Пользователь зарегистрирован");

        } catch (AuthorizationDeniedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Такой пользователь уже существует");
        }
    }


    //авторизация
    @PostMapping("/auth/login")
    ResponseEntity<?> login(@RequestBody JWTRequest jwtRequest) {
        log.info("Отправлен запрос на авторизацию");
        try {
            var auth = serviceAuth.authorize(jwtRequest);
            return ResponseEntity.ok(auth);

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());

        }
    }

    ;


    @PostMapping("auth/update/access")
    ResponseEntity<?> updateAccess(
            @RequestBody RefreshJwtRequest refreshJwtRequest){
        try {
            return ResponseEntity.ok(serviceAuth.updateAccessToken(refreshJwtRequest.getRefreshToken()));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("auth/update/refresh")
    ResponseEntity<?> updateRefresh(
            @RequestBody RefreshJwtRequest refreshJwtRequest){
        try {
            return ResponseEntity.ok(serviceAuth.updateRefreshToken(refreshJwtRequest.getRefreshToken()));
        }catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }



    @PostMapping("/user/{uuidUser}")
    ResponseEntity<String> getUser(
            @PathVariable UUID uuidUser
    ) {
        try {
            var user = serviceAuth.getUser(uuidUser);
            return ResponseEntity.ok(user.toString());
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.ok("Пользователь не найден");
        }
    }


}