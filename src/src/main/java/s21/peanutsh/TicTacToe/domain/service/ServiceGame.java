package s21.peanutsh.TicTacToe.domain.service;


import org.springframework.beans.factory.annotation.Autowired;
import s21.peanutsh.TicTacToe.datasource.mapper.Mapper;
import s21.peanutsh.TicTacToe.datasource.model.EntityModel;
import s21.peanutsh.TicTacToe.datasource.repository.RepositoryGame;
import s21.peanutsh.TicTacToe.datasource.repository.RepositoryModel;
import s21.peanutsh.TicTacToe.domain.model.CurrentGameModel;
import s21.peanutsh.TicTacToe.domain.model.GameField;
import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.domain.model.StateGame;
import s21.peanutsh.TicTacToe.web.controller.Controller;
import s21.peanutsh.TicTacToe.web.mapper.GameMapper;
import s21.peanutsh.TicTacToe.web.model.GameStatus;
import s21.peanutsh.TicTacToe.web.model.GameWinner;
import s21.peanutsh.TicTacToe.web.model.WebModel;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@org.springframework.stereotype.Service
public class ServiceGame implements IChangeGame {
    private static final Logger logger =  Logger.getLogger(Controller .class.getName());
    @Autowired
    private RepositoryGame repositoryGame;

    @Autowired
    private RepositoryModel repositoryModel;

    public boolean gameContains(UUID uuid) {
        return repositoryGame.findById(uuid).isPresent();
    }

    public void add(GameField gameField, UUID uuidGame) {
        repositoryGame.save(Mapper.convertGameToEntity(gameField, uuidGame));
    }

    //    -----------------------------------
    @Override
    public GameField generateNextStep(GameField gameField, UUID uuid) {
        if (gameField.getStatus() == GameStatus.GAME_OVER) {
            return gameField;
        }
        int bestScore = -1000000;
        int bestMove = -1;
        for (int i = 0; i < 9; i++) {
            var current = gameField.getCopy();
            int x = i % 3;
            int y = i / 3;
            if (current.getData()[x][y] == 0) {
                current.getData()[x][y] = 2;
                int score;
                if (gameWon(current, x, y)) {
                    score = 10;
                } else {
                    score = minMax1(current, 1, false);
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        gameField.getData()[bestMove % 3][bestMove / 3] = 2;
        if (gameWon(gameField, bestMove % 3, bestMove / 3)) {
            gameField.setStatus(GameStatus.GAME_OVER);
            gameField.setWinner(GameWinner.O);
        } else if (isFullField(gameField)) {
            gameField.setStatus(GameStatus.GAME_OVER);
            gameField.setWinner(GameWinner.DRAW);
        }

        repositoryGame.save(Mapper.convertGameToEntity(gameField, uuid));
        return gameField;
    }

    private int minMax1(GameField gameField, int deep, boolean isBot) {
        if (gameField.getStatus() == GameStatus.GAME_OVER) {
            if (gameField.getWinner() == GameWinner.O) return 1000 - deep;
            if (gameField.getWinner() == GameWinner.X) return -1000 + deep;
            if (gameField.getWinner() == GameWinner.DRAW) return 0;
        }

        if (isFullField(gameField)) {
            return 0;
        }

        int currentPlayer = isBot ? 2 : 1;

        int bestScore = -1000000 * (isBot ? 1 : -1);
        for (int i = 0; i < 9; i++) {
            var current = gameField.getCopy();
            int x = i % 3;
            int y = i / 3;
            if (current.getData()[x][y] == 0) {
                current.getData()[x][y] = currentPlayer;
                if (gameWon(current, x, y)) {
                    return isBot ? 100 + deep : -100 + deep;
                }
                int score = minMax1(current, deep + 1, !isBot);
                if (isBot) {
                    bestScore = Integer.max(bestScore, score);
                } else {
                    bestScore = Integer.min(bestScore, score);
                }
            }
        }
        return bestScore;
    }


    private int checkWinner(GameField gameField) {
        int[][] board = gameField.getData();
        for (int row = 0; row < 3; row++) {
            if (board[row][0] != 0 && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                return board[row][0];
            }
        }
        for (int col = 0; col < 3; col++) {
            if (board[0][col] != 0 && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                return board[0][col];
            }
        }
        if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0];
        }
        if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2];
        }
        return 0;
    }


    @Override
    public boolean validateStep(GameField gameField, UUID uuid) {
        var current = new CurrentGameModel(uuid, Mapper.convertEntityToGame(repositoryGame.findById(uuid).get()));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameField.getData()[i][j] != current.getGameField().getData()[i][j] && current.getGameField().getData()[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }


    public boolean isFullField(GameField gameField) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameField.getData()[i][j] == 0)
                    return false;
            }
        }
        return true;
    }


    private boolean gameWon(GameField gameField, int x, int y) {
        int[][] data = gameField.getData();
        if (gameField.getData()[x][y] == 0) {
            return false;
        }
        if (data[x][y] == data[0][y] && data[1][y] == data[x][y
                ] && data[2][y] == data[x][y
                ])
            return true;
        if (data[x][y] == data[x][0] && data[x][1] == data[x][y
                ] && data[x][2] == data[x][y
                ])
            return true;
        if (x == y) {
            if (data[x][y] == data[0][0] && data[1][1] == data[x][y
                    ] && data[2][2] == data[x][y
                    ])
                return true;
        }
        if (x + y == 2) {
            return data[x][y] == data[2][0] && data[1][1] == data[x][y
                    ] && data[0][2] == data[x][y
                    ];
        }
        return false;
    }


    public GameField getCurrentGame(UUID uuid) {
        return Mapper.convertEntityToGame(repositoryGame.findById(uuid).get());
    }


    public GameField update1(GameField gameField, UUID uuidGame) {

        if (checkWinner(gameField) == 1) {
            gameField.setStatus(GameStatus.GAME_OVER);
            gameField.setWinner(GameWinner.X);
        }

        repositoryGame.save(Mapper.convertGameToEntity(gameField, uuidGame));
        return gameField;
    }





    //----------------------------------------------------------------------------------------------------

    //ход компьютера
    private  void generateComputerMove(Model model){
        if (model.getGameOver()){
            return ;
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
        model.moveComputer(bestMove % 3,bestMove / 3);
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


    //----------------------------------------------------------------------------------------------------
    //создание игры
    public UUID createGame(boolean playingWithComputer) {
        logger.info(",kznm");

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
        if (model.getFirstPlayer().equals(uuidUser.toString()) || model.getSecondPlayer().equals(uuidUser.toString())) {
            throw new Exception("вы уже подключились к игре");
        }
        if (position == 1) {

            if (model.getFirstPlayer().equals("empty")) {
                model.setFirstPlayer(uuidUser.toString());
                if (model.getPlayingWithComputer()){
                    model.setSecondPlayer("computer");
                }
            } else {
                model.setSecondPlayer(uuidUser.toString());
            }
        } else {
            if (model.getSecondPlayer().equals("empty")) {
                model.setSecondPlayer(uuidUser.toString());
                if (model.getPlayingWithComputer()){
                    model.setFirstPlayer("computer");
                }
            } else {
                model.setFirstPlayer(uuidUser.toString());
            }
        }

        int countPlayers = 0;
        countPlayers+= model.getFirstPlayer().equals("empty")?0:1;
        countPlayers+= model.getSecondPlayer().equals("empty")?0:1;
        if (countPlayers==2){
            model.setStateGame(StateGame.TURN_FIRST_PLAYER);

        }
        var toEntity = Mapper.convertModelToEntity(model);
        toEntity.setId(entityModel.get().getId());
        repositoryModel.save(toEntity);
    }


    //получение текущей игры
    public WebModel getCurrentModel(UUID uuidUser, UUID uuidGame) throws Exception {
        var game = repositoryModel.findByUuidGameAndUuidUser(uuidGame, false, uuidUser.toString());
        if (game.isEmpty()) {
            throw new Exception("такой игры не существует");
        }
        return GameMapper.convertDomainToWeb(
                Mapper.convertEntityToModel(game.get())
        );
    }


    //получение всех доступных игр
    public List<WebModel> getAvailableGames(UUID uuidUser) {
        return repositoryModel.findAllByStateGameAndFirstPlayerIsNotAndSecondPlayerIsNot(
                        StateGame.WAITING_PLAYERS, uuidUser.toString(), uuidUser.toString()
                )
                .stream()
                .map(Mapper::convertEntityToModel)
                .map(GameMapper::convertDomainToWeb)
                .toList();
    }


    //получение всех текущих игр
    public List<UUID> getAllCurrentGames(UUID uuidUser) throws Exception {
        var list = repositoryModel.findCurrentGamesForUser(false, uuidUser.toString());
        if (list.isEmpty()) {
            throw new Exception("нет текущих игр");
        }

        return list.stream().map(Mapper::convertEntityToModel)
                .map(GameMapper::convertDomainToWeb)
                .map(a -> ((WebModel) a).getUuidGame())
                .toList();
    }





    //----------------------------------------------------------------------------------------------------

    //проверка, существует ли такая игра
    public Long isValidityGame(UUID uuidGame, UUID uuidUser, WebModel webModel) throws Exception {
      var entity=  repositoryModel.findByUuidGame(uuidGame);
      if (entity.isEmpty()){
          throw new Exception("такой игры не существует");
      }
      var model = Mapper.convertEntityToModel(entity.get());

      if (!model.getFirstPlayer().equals(uuidUser.toString())&&
              model.getSecondPlayer().equals(uuidUser.toString()) ){
          throw new RuntimeException("Вы не являетесь игроком");
      }
        if(!model.yourTurn(uuidUser.toString())){
            throw new RuntimeException("не твой ход");
        }
        if(model.getGameOver()){
            throw new RuntimeException("игра закончилась");
        }
        if( !model.isValidityChange(GameMapper.convertWebToDomain(webModel))){
            throw new RuntimeException("не корректные данные");
        }
      return entity.get().getId();
    }


    public WebModel update(WebModel webModel,UUID uuidUser,long id) {
        //получили поле
        var model = GameMapper.convertWebToDomain(webModel);
        //проверили победа или нет
        if (model.isWin()){
            model.setGameOver(true);
            model.detectWinPlayer(uuidUser);
        }
        // проверили, заполнилось или нет
        model.isFullField();

        if (!model.getGameOver()) {
            if (model.getPlayingWithComputer()){
                generateComputerMove(model);
            }else {
                model.detectNextTurnPlayer(uuidUser);
            }
        }
        var toEntity = Mapper.convertModelToEntity(model);
        toEntity.setId(id);
        repositoryModel.save(toEntity);

        return GameMapper.convertDomainToWeb(model);
    }
}
