package s21.peanutsh.TicTacToe.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String login;
    private String password;
}
