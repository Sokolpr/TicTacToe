package s21.peanutsh.TicTacToe.domain.service;

import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.web.model.WebModel;

import java.util.UUID;

public interface IChangeGame {
    void generateComputerMove(Model model);

    public void isValidityGame(UUID uuidGame, UUID uuidUser, WebModel webModel) throws Exception;

}
