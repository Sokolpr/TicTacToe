package s21.peanutsh.TicTacToe.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import s21.peanutsh.TicTacToe.domain.service.ServiceGame;
import s21.peanutsh.TicTacToe.web.model.WebModel;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());
    private final ServiceGame service;


    public Controller(ServiceGame service) {
        this.service = service;
    }

    @PostMapping("/game/{uuidGame}")
    public ResponseEntity<?> update(
            @PathVariable UUID uuidGame,
            @RequestBody WebModel webModel
    ) {

        try {
            service.isValidityGame(uuidGame,
                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                    webModel);
            return ResponseEntity.
                    ok(
                            service.update(webModel,
                                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                            ));

        } catch (Exception e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }

    }





    //получение доступных игр
    @PostMapping("/games")
    public ResponseEntity<?> pullAvailableGames() {


        var list = service.getAvailableGames(
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
        return ResponseEntity.ok(service.createGame(bot));
    }

    //присоединение к игре
    @PostMapping("/game/{uuidGame}/join")
    public ResponseEntity<?> joinGame(
            @PathVariable UUID uuidGame,
            @RequestBody Integer position
    ) {
        try {
            service.joinToGame((UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), uuidGame, position);

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok("Вы присоединились к игре");
    }

    //получения текущей игры
    @PatchMapping("/game/{uuidGame}/get")
    public ResponseEntity<?> getGame(
            @PathVariable UUID uuidGame
    ) {
        try {
            return ResponseEntity.ok(service.getCurrentModel(
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
            return ResponseEntity.ok(service.getAllCurrentGames(
                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
            ));

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
