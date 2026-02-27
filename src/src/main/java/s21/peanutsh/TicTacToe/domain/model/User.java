package s21.peanutsh.TicTacToe.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {
    private UUID uuid;
    private String login;
    private String password;
    private Set<Role> roles;
}
