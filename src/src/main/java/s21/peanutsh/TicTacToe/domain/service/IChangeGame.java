package s21.peanutsh.TicTacToe.domain.service;

import s21.peanutsh.TicTacToe.domain.model.GameField;

import java.util.UUID;

public interface IChangeGame {
    public GameField generateNextStep(GameField gameField, UUID uuid);
    public boolean validateStep(GameField gameField, UUID uuid);

}
