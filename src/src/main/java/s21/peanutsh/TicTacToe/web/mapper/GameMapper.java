package s21.peanutsh.TicTacToe.web.mapper;

import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.web.model.WebModel;

import java.util.UUID;

public class GameMapper {

    public static WebModel convertDomainToWeb(Model model) {
        WebModel webModel = WebModel.builder()
                .uuidGame(model.getUuidGame())
                .firstPlayer_X(model.getFirstPlayer())
                .secondPlayer_O(model.getSecondPlayer())
                .playingWithBot(model.getPlayingWithComputer())
                .stateGame(model.getStateGame())
                .gameOver(model.getGameOver())
                .build();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(model.getField()[i], 0, webModel.getField()[i], 0, 3);
        }
        return webModel;
    }


    public static Model convertWebToDomain(WebModel webModel) {
        Model model = Model.builder()
                .field(new int[3][3])
                .uuidGame(webModel.getUuidGame())
                .firstPlayer(webModel.getFirstPlayer_X())
                .secondPlayer(webModel.getSecondPlayer_O())
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
