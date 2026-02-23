package s21.peanutsh.TicTacToe.domain.service;


import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.web.model.WebModel;

import java.util.List;
import java.util.UUID;

public interface GameService {
    //ход компьютера
    void generateComputerMove(Model model);

    //создание игры
    UUID createGame(boolean playingWithComputer);

    //подключение к игре
    void joinToGame(UUID uuidUser, UUID uuidGame, Integer position) throws Exception;

    //получение текущей игры
    WebModel getCurrentModel(UUID uuidUser, UUID uuidGame) throws Exception;

    //получение всех доступных игр
    List<WebModel> getAvailableGames(UUID uuidUser);

    //получение всех текущих игр
    List<UUID> getAllCurrentGames(UUID uuidUser) throws Exception;

    List<WebModel> getAllOverGames() throws Exception;

    // получение всех завершенных игр для пользователя
    List<WebModel> getAllOverGamesForUser(UUID uuidUser) throws Exception;

    //проверка, существует ли такая игра
    void isValidityGame(UUID uuidGame, UUID uuidUser, WebModel webModel) throws Exception;

    //Обновление поля
    WebModel update(WebModel webModel, UUID uuidUser);

}
