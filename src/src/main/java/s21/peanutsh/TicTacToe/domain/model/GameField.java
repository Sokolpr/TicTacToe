package s21.peanutsh.TicTacToe.domain.model;

import lombok.Getter;
import lombok.Setter;
import s21.peanutsh.TicTacToe.web.model.GameStatus;
import s21.peanutsh.TicTacToe.web.model.GameWinner;

@Getter
@Setter
public class GameField {
    private int[][] data;
    private GameStatus status;
    private GameWinner winner;


    public GameField() {
        data = new int[3][3];
    }
    private GameField(GameField gf) {
        data = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(gf.getData()[i], 0, data[i], 0, 3);
        }
        status = gf.getStatus();
        winner = gf.getWinner();
    }
    public GameField getCopy(){
        return new GameField(this);
    }

    public GameField(int[][] data, GameStatus status, GameWinner winner) {
        this.data = data;
        this.status = status;
        this.winner = winner;
    }
}
