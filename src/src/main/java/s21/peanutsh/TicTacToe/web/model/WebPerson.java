package s21.peanutsh.TicTacToe.web.model;

import lombok.*;

import java.util.UUID;

@Setter
@Builder
@AllArgsConstructor
public class WebPerson {
    private String login;
    private UUID uuid;

    @Override
    public String toString() {
        return "Login:" + login + "\nUUID: " + uuid;
    }
}
