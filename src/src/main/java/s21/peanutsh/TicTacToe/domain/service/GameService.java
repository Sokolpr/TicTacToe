package s21.peanutsh.TicTacToe.domain.service;


import s21.peanutsh.TicTacToe.domain.model.Model;
import s21.peanutsh.TicTacToe.web.model.WebModel;

import java.util.List;
import java.util.UUID;

public interface GameService {
    //ход компьютера
    public void generateComputerMove(Model model);
    //создание игры
    public UUID createGame(boolean playingWithComputer);
    //подключение к игре
    public void joinToGame(UUID uuidUser, UUID uuidGame, Integer position) throws Exception;
    //получение текущей игры
    public WebModel getCurrentModel(UUID uuidUser, UUID uuidGame) throws Exception;
    //получение всех доступных игр
    public List<WebModel> getAvailableGames(UUID uuidUser);
    //получение всех текущих игр
    public List<UUID> getAllCurrentGames(UUID uuidUser) throws Exception;

    public List<WebModel> getAllOverGames() throws Exception;

    // получение всех завершенных игр для пользователя
    public List<WebModel> getAllOverGamesForUser(UUID uuidUser) throws Exception;

    //проверка, существует ли такая игра
    public void isValidityGame(UUID uuidGame, UUID uuidUser, WebModel webModel) throws Exception;

    //Обновление поля
    public WebModel update(WebModel webModel, UUID uuidUser);

}
