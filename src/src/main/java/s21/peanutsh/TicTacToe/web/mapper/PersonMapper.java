package s21.peanutsh.TicTacToe.web.mapper;

import s21.peanutsh.TicTacToe.web.model.WebPerson;

import java.util.UUID;

public class PersonMapper {
    public static WebPerson entityToWeb(String login, UUID uuid) {
        return new WebPerson(login, uuid);
    }
}
