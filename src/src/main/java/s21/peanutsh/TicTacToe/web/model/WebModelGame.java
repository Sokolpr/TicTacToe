package s21.peanutsh.TicTacToe.web.model;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class WebModelGame {
    private int[][] field;
    private GameStatus status;
    private GameWinner winner;
    private GameStatus stateGame;
    private UUID uuid;

    public WebModelGame(UUID uuid) {
        this.uuid = uuid;
        this.field = new int[3][3];
        status = GameStatus.PLAYING;
    }

    public WebModelGame() {
        this.field = new int[3][3];
        status = GameStatus.PLAYING;
    }

    public WebModelGame(int[][] field) {
        this.field = field;
    }

}
