package s21.peanutsh.TicTacToe.web.model;


import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class JWTRequest {
    private String login;
    private String password;

}
