package s21.peanutsh.TicTacToe.domain.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class Model {
    private int[][] field;
    private StateGame stateGame;
    private UUID uuidGame;
    private UUID firstPlayer;
    private Boolean playingWithComputer;
    private UUID secondPlayer;
    private Boolean gameOver;
    private LocalDate createDate;


    public Model(boolean playingWithComputer) {
        firstPlayer = null;
        secondPlayer = null;
        this.uuidGame = UUID.randomUUID();
        field = new int[3][3];
        this.playingWithComputer = playingWithComputer;
        stateGame = StateGame.WAITING_PLAYERS;
        gameOver = false;
        createDate = LocalDate.now();
    }


    public Model(Model model) {
        this.stateGame = model.getStateGame();
        this.uuidGame = model.getUuidGame();
        this.firstPlayer = model.getFirstPlayer();
        this.secondPlayer = model.getSecondPlayer();
        this.playingWithComputer = model.getPlayingWithComputer();
        this.gameOver = model.getGameOver();
        this.createDate = model.getCreateDate();
        this.field = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(model.getField()[i], 0, this.field[i], 0, 3);
        }
    }


    public Model getCopy() {
        return new Model(this);
    }

    public Boolean isFullField() {
        if (gameOver) return false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == 0) {
                    return false;
                }
            }
        }
        gameOver = true;
        stateGame = StateGame.ROW;
        return true;
    }

    public void moveComputer(int x, int y) {
        field[x][y] = detectComputerMove();
        if (isWin()) {
            gameOver = true;
            stateGame = detectComputerWinner();
        } else {
            isFullField();
        }
    }

    public boolean yourTurn(UUID uuidUser) {

        if (Objects.equals(uuidUser, firstPlayer)) {
            return stateGame == StateGame.TURN_FIRST_PLAYER;
        } else if (uuidUser.equals(secondPlayer)) {
            return stateGame == StateGame.TURN_SECOND_PLAYER;
        }

        return false;
    }

    public StateGame detectComputerWinner() {
        if (firstPlayer == null) {
            return StateGame.WINNING_FIRST_PLAYER;
        } else {
            return StateGame.WINNING_SECOND_PLAYER;
        }
    }

    public int detectComputerMove() {
        if (firstPlayer == null) {
            return 1;
        } else {
            return 2;
        }
    }

    public int detectUserMove() {
        if (firstPlayer == null) {
            return 2;
        } else {
            return 1;
        }
    }

    public void detectWinPlayer(UUID uuidUser) {
        if (firstPlayer.equals(uuidUser)) {
            stateGame = StateGame.WINNING_FIRST_PLAYER;
        } else {
            stateGame = StateGame.WINNING_SECOND_PLAYER;
        }
    }

    public void detectNextTurnPlayer(UUID uuidUser) {
        if (Objects.equals(uuidUser, firstPlayer)) {
            stateGame = StateGame.TURN_SECOND_PLAYER;
        } else {
            stateGame = StateGame.TURN_FIRST_PLAYER;
        }
    }

    private int detectUser(UUID uuidUser) {
        if (Objects.equals(uuidUser, firstPlayer)) {
            return 1;
        } else {
            return 2;
        }
    }

    public boolean isWin() {
        if (gameOver) return false;
        // проверка горизонтальных линий
        for (int i = 0; i < 3; i++) {
            if (field[i][0] != 0 &&
                    field[i][0] == field[i][1] &&
                    field[i][0] == field[i][2]
            ) return true;
        }

        //проверка вертикальных линий
        for (int i = 0; i < 3; i++) {
            if (field[0][i] != 0 &&
                    field[0][i] == field[1][i] &&
                    field[0][i] == field[2][i]
            ) return true;
        }

        //проверка первой диагонали
        if (field[0][0] != 0 &&
                field[0][0] == field[1][1] &&
                field[0][0] == field[2][2]
        ) return true;

        //проверка второй диагонали
        if (field[0][2] != 0 &&
                field[0][2] == field[1][1] &&
                field[0][2] == field[2][0]
        ) return true;

        return false;
    }

    public boolean inValidityChange(Model model, UUID uuidUser) {
        if (model == null) {
            return true;
        }

        if (!Objects.equals(uuidGame, model.getUuidGame())) {
            return true;
        }

        if (stateGame != model.getStateGame()) {
            return true;
        }

        if (!Objects.equals(firstPlayer, model.getFirstPlayer())) {
            return true;
        }

        if (!Objects.equals(secondPlayer, model.getSecondPlayer())) {
            return true;
        }

        if (gameOver != model.getGameOver()) {
            return true;
        }

        if (playingWithComputer != model.getPlayingWithComputer()) {
            return true;
        }


        int change = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (field[i][j] == 0) {
                    if (model.getField()[i][j] == 0) {
                        continue;
                    }
                    if (model.getField()[i][j] == detectUser(uuidUser)) {
                        change++;
                    } else {
                        return true;
                    }
                } else if (field[i][j] == 1 || field[i][j] == 2) {
                    if (field[i][j] != model.getField()[i][j]) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }

        return change != 1;
    }


}
