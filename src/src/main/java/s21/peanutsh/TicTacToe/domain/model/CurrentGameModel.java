package s21.peanutsh.TicTacToe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CurrentGameModel {
    private UUID uuid;
    private GameField gameField;

}
