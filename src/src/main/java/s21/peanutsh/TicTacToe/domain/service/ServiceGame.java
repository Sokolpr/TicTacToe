package s21.peanutsh.TicTacToe.domain.service;


import org.springframework.beans.factory.annotation.Autowired;
import s21.peanutsh.TicTacToe.datasource.mapper.Mapper;
import s21.peanutsh.TicTacToe.datasource.repository.RepositoryModel;
import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.domain.model.StateGame;
import s21.peanutsh.TicTacToe.web.controller.Controller;
import s21.peanutsh.TicTacToe.web.mapper.GameMapper;
import s21.peanutsh.TicTacToe.web.model.WebModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class ServiceGame implements IChangeGame {
    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    @Autowired
    private RepositoryModel repositoryModel;


    // Детальное логирование для отладки
//        logger.info("=== DEBUG isValidityCheck ===");
//        logger.info("Original model UUID: {}" + model.getUuidGame());
//        logger.info("Original firstPlayer: {}" + model.getFirstPlayer());
//        logger.info("Original secondPlayer: {}" + model.getSecondPlayer());
//        logger.info("Original gameOver: {}" + model.getGameOver());
//        logger.info("Original playingWithComputer: {}" + model.getPlayingWithComputer());
//        logger.info("Original stateGame: {}" + model.getStateGame());
//        logger.info("Original field:");
//        for (int l = 0; l < 3; l++) {
//            logger.info(Arrays.toString(model.getField()[l]));
//        }


    //ход компьютера
    public void generateComputerMove(Model model) {
        if (model.getGameOver()) {
            return;
        }


        int bestScore = -1000000;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            var current = model.getCopy();

            int x = i % 3;
            int y = i / 3;
            if (current.getField()[x][y] == 0) {
                current.getField()[x][y] = current.detectComputerMove();
                int score;
                if (current.isWin()) {
                    score = 10;
                } else {
                    score = minMax(current, 1, false);
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        model.moveComputer(bestMove % 3, bestMove / 3);
    }

    //реализация min/max
    private int minMax(Model model, int deep, boolean isBot) {
        if (model.getGameOver()) {
            if (model.getStateGame() == model.detectComputerWinner()) return 1000 - deep;
            else if (model.getStateGame() == StateGame.ROW) return 0;
            else return -1000 + deep;

        }

        if (model.isFullField()) {
            return 0;
        }
        int currentPlayer = isBot ? model.detectComputerMove() : model.detectUserMove();
        int bestScore = -1000000 * (isBot ? 1 : -1);
        for (int i = 0; i < 9; i++) {
            var current = model.getCopy();
            int x = i % 3;
            int y = i / 3;
            if (current.getField()[x][y] == 0) {
                current.getField()[x][y] = currentPlayer;
                if (current.isWin()) {
                    return isBot ? 100 + deep : -100 + deep;
                }
                int score = minMax(current, deep + 1, !isBot);
                if (isBot) {
                    bestScore = Integer.max(bestScore, score);
                } else {
                    bestScore = Integer.min(bestScore, score);
                }
            }
        }
        return bestScore;
    }


    //создание игры
    public UUID createGame(boolean playingWithComputer) {

        Model model = new Model(playingWithComputer);
        repositoryModel.save(Mapper.convertModelToEntity(model));
        return model.getUuidGame();
    }


    //подключение к игре
    public void joinToGame(UUID uuidUser, UUID uuidGame, Integer position) throws Exception {
        var entityModel = repositoryModel.findByUuidGameAndStateGame(uuidGame, StateGame.WAITING_PLAYERS);
        if (entityModel.isEmpty()) {
            throw new Exception("невозможно подключиться");
        }

        var model = Mapper.convertEntityToModel(entityModel.get());
        if (model.getStateGame() != StateGame.WAITING_PLAYERS) {
            throw new Exception("невозможно подключиться, игра уже идет");
        }
        if (model.getFirstPlayer() == uuidUser || model.getSecondPlayer() == uuidUser) {
            throw new Exception("вы уже подключились к игре");
        }


        if (position == 1) {
            if (model.getFirstPlayer() == null) {
                model.setFirstPlayer(uuidUser);
            } else {
                model.setSecondPlayer(uuidUser);
            }
        } else {
            if (model.getSecondPlayer() == null) {
                model.setSecondPlayer(uuidUser);
            } else {
                model.setFirstPlayer(uuidUser);
            }
        }

        int countPlayers = 0;

        countPlayers += model.getFirstPlayer() == null ? 0 : 1;
        countPlayers += model.getSecondPlayer() == null ? 0 : 1;

        if (countPlayers == 2) {
            model.setStateGame(StateGame.TURN_FIRST_PLAYER);
        } else if (countPlayers == 1 && model.getPlayingWithComputer()) {
            if (position == 2) {
                generateComputerMove(model);
                model.setStateGame(StateGame.TURN_SECOND_PLAYER);
            }
        }
        var toEntity = Mapper.convertModelToEntity(model);
        repositoryModel.save(toEntity);
    }


    //получение текущей игры
    public WebModel getCurrentModel(UUID uuidUser, UUID uuidGame) throws Exception {
        var game = repositoryModel.findByUuidGameAndUuidUser(uuidGame, uuidUser);
        if (game.isEmpty()) {
            throw new Exception("такой игры не существует");
        }

        return GameMapper.convertDomainToWeb(
                Mapper.convertEntityToModel(game.get())
        );
    }


    //получение всех доступных игр
    public List<WebModel> getAvailableGames(UUID uuidUser) {
        return repositoryModel.findGames(
                        StateGame.WAITING_PLAYERS, uuidUser
                )
                .stream()
                .map(Mapper::convertEntityToModel)
                .map(GameMapper::convertDomainToWeb)
                .toList();
    }


    //получение всех текущих игр
    public List<UUID> getAllCurrentGames(UUID uuidUser) throws Exception {
        var list = repositoryModel.findCurrentGamesForUser(false, uuidUser);
        if (list.isEmpty()) {
            throw new Exception("нет текущих игр");
        }

        return list.stream().map(Mapper::convertEntityToModel)
                .map(GameMapper::convertDomainToWeb)
                .map(a -> ((WebModel) a).getUuidGame())
                .toList();
    }


    //проверка, существует ли такая игра
    public void isValidityGame(UUID uuidGame, UUID uuidUser, WebModel webModel) throws Exception {
        var entity = repositoryModel.findByUuidGame(uuidGame);
        if (entity.isEmpty()) {
            throw new Exception("такой игры не существует");
        }

        var model = Mapper.convertEntityToModel(entity.get());


        if (!Objects.equals(uuidUser, model.getFirstPlayer()) && !Objects.equals(uuidUser, model.getSecondPlayer())) {
            throw new RuntimeException("Вы не являетесь игроком");
        }

        if (model.getStateGame() == StateGame.WAITING_PLAYERS) {
            throw new RuntimeException("Игра еще не началась");
        }

        if (!model.yourTurn(uuidUser)) {
            throw new RuntimeException("Не твой ход");
        }

        for (int l = 0; l < 3; l++) {
            logger.info(Arrays.toString(model.getField()[l]));
        }
        if (model.getGameOver()) {
            throw new RuntimeException("Игра закончилась");
        }
        Model convertedModel = GameMapper.convertWebToDomain(webModel);

        if (!model.isValidityChange(convertedModel, uuidUser)) {
            logger.warning("Валидация не пройдена!");
            throw new RuntimeException("Не корректные данные");
        } else {
            logger.info("Валидация пройдена успешно");
        }


    }


    public WebModel update(WebModel webModel, UUID uuidUser) {
        //получили поле
        var model = GameMapper.convertWebToDomain(webModel);
        //проверили победа или нет
        if (model.isWin()) {
            model.setGameOver(true);
            model.detectWinPlayer(uuidUser);
        }
        // проверили, заполнилось или нет
        model.isFullField();

        if (!model.getGameOver()) {
            if (model.getPlayingWithComputer()) {
                generateComputerMove(model);
            } else {
                model.detectNextTurnPlayer(uuidUser);
            }
        }


        var toEntity = Mapper.convertModelToEntity(model);
        repositoryModel.save(toEntity);

        return GameMapper.convertDomainToWeb(model);

    }

}
