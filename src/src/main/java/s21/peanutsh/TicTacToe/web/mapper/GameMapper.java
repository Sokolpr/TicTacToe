package s21.peanutsh.TicTacToe.web.mapper;
import s21.peanutsh.TicTacToe.domain.model.GameField;
import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.web.model.GameStatus;
import s21.peanutsh.TicTacToe.web.model.WebModel;
import s21.peanutsh.TicTacToe.web.model.WebModelGame;

import java.util.UUID;

public class GameMapper {
    public static WebModelGame domainToWeb(GameField model){
    WebModelGame webModelGame = new WebModelGame();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(model.getData()[i], 0, webModelGame.getField()[i], 0, 3);
        }
        webModelGame.setStatus(model.getStatus());
        if (model.getStatus()== GameStatus.GAME_OVER){
            webModelGame.setWinner(model.getWinner());
        }
    return webModelGame;
    }
    public static GameField webToDomain(WebModelGame webModelGame){
        GameField gameField = new GameField();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(webModelGame.getField()[i], 0, gameField.getData()[i], 0, 3);
        }
        gameField.setStatus(webModelGame.getStatus());
        if (webModelGame.getStatus()==GameStatus.GAME_OVER){
            gameField.setWinner(webModelGame.getWinner());
        }
        return gameField;
    }

    public static WebModel convertDomainToWeb(Model model){
        WebModel webModel = WebModel.builder()
                .uuidGame(model.getUuidGame())
                .firstPlayer_X(UUID.fromString(model.getFirstPlayer()))
                .secondPlayer_O(UUID.fromString(model.getSecondPlayer()))
                .playingWithBot(model.getPlayingWithComputer())
                .stateGame(model.getStateGame())
                .gameOver(model.getGameOver())
                .build();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(model.getField()[i],0,webModel.getField()[i],0,3);
        }
        return webModel;
    }


    public static Model convertWebToDomain(WebModel webModel) {
        Model model = Model.builder()
                .uuidGame(webModel.getUuidGame())
                .firstPlayer(webModel.getFirstPlayer_X().toString())
                .secondPlayer(webModel.getSecondPlayer_O().toString())
                .playingWithComputer(webModel.getPlayingWithBot())
                .stateGame(webModel.getStateGame())
                .gameOver(webModel.getGameOver())
                .build();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(webModel.getField()[i], 0, model.getField()[i], 0, 3);
        }
        return model;
    }
}
