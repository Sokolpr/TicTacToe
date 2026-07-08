package s21.peanutsh.TicTacToe.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import s21.peanutsh.TicTacToe.domain.service.AuthService;
import s21.peanutsh.TicTacToe.web.model.JWTRequest;
import s21.peanutsh.TicTacToe.web.model.RefreshJwtRequest;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;


    //регистрация
    @PostMapping("/auth/signup")
    ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        log.info("Отправлен запрос на регистрацию");
        try {
            authService.registration(signUpRequest);
            return ResponseEntity.ok("Пользователь зарегистрирован");

        } catch (AuthorizationDeniedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //авторизация
    @PostMapping("/auth/login")
    ResponseEntity<?> login(@RequestBody JWTRequest jwtRequest) {
        log.info("Отправлен запрос на авторизацию");
        try {
            var auth = authService.login(jwtRequest);
            return ResponseEntity.ok(auth);

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());

        }
    }

    ;


    @PostMapping("auth/update/access")
    ResponseEntity<?> updateAccess(
            @RequestBody RefreshJwtRequest refreshJwtRequest) {
        try {
            return ResponseEntity.ok(authService.updateAccessToken(refreshJwtRequest.getRefreshToken()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PostMapping("auth/update/refresh")
    ResponseEntity<?> updateRefresh(
            @RequestBody RefreshJwtRequest refreshJwtRequest) {
        try {
            return ResponseEntity.ok(authService.updateRefreshToken(refreshJwtRequest.getRefreshToken()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }


    @PostMapping("/user/{uuidUser}")
    ResponseEntity<String> getUserByUuid(
            @PathVariable UUID uuidUser
    ) {
        try {
            var user = authService.getUserByUuid(uuidUser);
            return ResponseEntity.ok(user.toString());
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.ok("Пользователь не найден");
        }
    }

    @PostMapping("/user/my")
    ResponseEntity<String> getMyProfile() {
        try {
            var user = authService.getUserByUuid(authService.getAuth().getUuid());
            return ResponseEntity.ok(user.toString());
        } catch (UsernameNotFoundException exception) {
            return ResponseEntity.ok("Пользователь не найден");
        }
    }


}