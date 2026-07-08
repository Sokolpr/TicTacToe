package s21.peanutsh.TicTacToe.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import s21.peanutsh.TicTacToe.domain.service.GameService;
import s21.peanutsh.TicTacToe.web.model.WebModel;
import s21.peanutsh.TicTacToe.web.model.WinModel;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;



    @GetMapping("/game/{uuidGame}")
    public ResponseEntity<?> update(
            @PathVariable UUID uuidGame,
            @RequestBody WebModel webModel
    ) {
        try {
            gameService.isValidityGame(uuidGame,
                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                    webModel);
            return ResponseEntity.
                    ok(
                            gameService.update(webModel,
                                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                            ));

        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }

    }


    //получение доступных игр
    @PostMapping("/game/all")
    public ResponseEntity<?> pullAvailableGames() {
        var list = gameService.getAvailableGames(
                (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        if (list.isEmpty()) {
            return ResponseEntity.ok("Доступных игр нет");
        } else {
            return ResponseEntity.ok(list.toString());
        }
    }

    //создание игры
    @PostMapping("/game/create")
    public ResponseEntity<?> addGame(
            @RequestBody boolean bot
    ) {
        return ResponseEntity.ok(gameService.createGame(bot));
    }

    //присоединение к игре
    @PostMapping("/game/{uuidGame}/join")
    public ResponseEntity<?> joinGame(
            @PathVariable UUID uuidGame,
            @RequestBody Integer position
    ) {
        try {
            gameService.joinToGame((UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), uuidGame, position);

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok("Вы присоединились к игре");
    }

    //получения текущей игры
    @GetMapping("/game/{uuidGame}/get")
    public ResponseEntity<?> getGame(
            @PathVariable UUID uuidGame
    ) {
        try {
            return ResponseEntity.ok(gameService.getCurrentModel(
                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                    uuidGame
            ));

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //получения всех текущих игр
    @PostMapping("/game/get_current")
    public ResponseEntity<List<?>> getAllGames(
    ) {
        try {
            return ResponseEntity.ok(gameService.getAllCurrentGames(
                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            ));

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // получение всех завершенных игр по uuid пользователя
    @PostMapping("/game/history")
    public ResponseEntity<?> getAllOverGamesForUser(
    ) {
        try {
            return ResponseEntity.ok(gameService.getAllOverGamesForUser(
                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            ));
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }


    // получение всех завершенных игр
    @PostMapping("/game/game_over")
    public ResponseEntity<?> getAllOverGames(

    ) {
        try {
            return ResponseEntity.ok(gameService.getAllOverGames());
        } catch (Exception e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }


    //получение лучших пользователей
    @PostMapping("/game/best")
    public ResponseEntity<?> getBestPlayers(
            @RequestBody int count
    )

    {
        try{
            var list=  gameService.getBestPlayers(count);
            return  ResponseEntity.ok(list.stream().map(WinModel::toString).toList());
        }catch (Exception e){
            return ResponseEntity.ok("");
        }
    }

}

