package s21.peanutsh.TicTacToe.web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import s21.peanutsh.TicTacToe.domain.service.ServiceGame;
import s21.peanutsh.TicTacToe.web.model.GameStatus;
import s21.peanutsh.TicTacToe.web.mapper.GameMapper;
import s21.peanutsh.TicTacToe.web.model.SignUpRequest;
import s21.peanutsh.TicTacToe.web.model.WebModel;
import s21.peanutsh.TicTacToe.web.model.WebModelGame;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    private final ServiceGame service;


    //    @Autowired
    public Controller(ServiceGame service) {
        this.service = service;
    }


    @PostMapping("/game/{uuidGame}")
    public ResponseEntity<WebModelGame> playGame(
            @PathVariable UUID uuidGame,
            @RequestBody WebModelGame model
    ) {

        logger.info("1 " + model.getStatus());

        //Проверяем существует ли такая игра или нет
        if (!service.gameContains(uuidGame)) {
            service.add(GameMapper.webToDomain(model), uuidGame);
        } else {

            //Проверяем мб игра закончилась
            if (GameMapper.domainToWeb(service.getCurrentGame(uuidGame)).getStatus() == GameStatus.GAME_OVER) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GameMapper.domainToWeb(service.getCurrentGame(uuidGame)));//написать что игра уже закончена
            }
        }

        logger.info("2 " + model.getStatus());


        //Проверяем валидность данных
        if (!service.validateStep(GameMapper.webToDomain(model), uuidGame)) {
            var res = GameMapper.domainToWeb(service.getCurrentGame(uuidGame));
            return ResponseEntity.status(HttpStatus.CONFLICT).body(res);
        }

        logger.info("3 " + model.getStatus());


        //Отправляем новые данные
        model = GameMapper.domainToWeb(service.update1(GameMapper.webToDomain(model), uuidGame));


        // Проверяем закончилась ли она победой игрока
        if (model.getStatus() == GameStatus.GAME_OVER) {
            return ResponseEntity.status(HttpStatus.OK).body(model);
        }

        logger.info("4 " + model.getStatus());

        //Компьютер делает свой ход
        model = GameMapper.domainToWeb(service.generateNextStep(GameMapper.webToDomain(model), uuidGame));


        logger.info("5 " + model.getStatus());


        // Проверяем закончилась ли она победой компьютера
        if (model.getStatus() == GameStatus.GAME_OVER) {
            return ResponseEntity.status(HttpStatus.OK).body(model);
        }

        logger.info("6 " + model.getStatus());


        return ResponseEntity.ok(model);
    }


    @PostMapping("/game/{uuidGame}")
    public ResponseEntity<String> update(
            @PathVariable UUID uuidGame,
            @RequestBody WebModel webModel
    ) {
        try {
            return ResponseEntity.ok(
                    service.update(webModel, (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                            service.isValidityGame(
                                    uuidGame,
                                    (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                                    webModel)
                    ).toString());

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }



    }


    //получение доступных игр
    @PostMapping("/games")
    public ResponseEntity<String> pullAvailableGames() {
        var list = service.getAvailableGames(
                (UUID) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );
        if (list.isEmpty()) {
            return ResponseEntity.ok("Доступных игр нет");
        } else {
            return ResponseEntity.ok(list.toString());
        }

    }


    //получение шаблона игр
    @PostMapping("/person")
    public ResponseEntity<SignUpRequest> getPerson() {
        return ResponseEntity.ok(new SignUpRequest("roma", "123"));
    }

    //создание игры
    @PostMapping("/game/create")
    public ResponseEntity<UUID> addGame(
            @RequestBody boolean bot
    ) {
        return ResponseEntity.ok(service.createGame(bot));
    }

    //присоединение к игре
    @PostMapping("/game/{uuidGame}/join")
    public ResponseEntity<String> joinGame(
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
    @PostMapping("/game/{uuidGame}/get")
    public ResponseEntity<WebModel> getGame(
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
    public ResponseEntity<List<UUID>> getAllGames(
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
