package s21.peanutsh.TicTacToe.web.model;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Setter
@Builder
@AllArgsConstructor
@ToString
public class WebPerson {
    private String login;
    private UUID uuid;
}
