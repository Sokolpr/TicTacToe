package s21.peanutsh.TicTacToe.web.model;


import lombok.*;
import s21.peanutsh.TicTacToe.domain.model.StateGame;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WebModel {
    private UUID uuidGame;
    private int[][] field;
    private UUID FirstPlayer_X;
    private UUID SecondPlayer_O;
    private Boolean playingWithBot;
    private StateGame stateGame;
    private Boolean gameOver;
    private LocalDate createDate;

    @Builder
    public WebModel(UUID uuidGame, UUID firstPlayer_X, UUID secondPlayer_O, boolean playingWithBot, StateGame stateGame, Boolean gameOver, LocalDate createDate) {
        field = new int[3][3];
        this.uuidGame = uuidGame;
        FirstPlayer_X = firstPlayer_X;
        SecondPlayer_O = secondPlayer_O;
        this.playingWithBot = playingWithBot;
        this.stateGame = stateGame;
        this.gameOver = gameOver;
        this.createDate = createDate;
    }
}
