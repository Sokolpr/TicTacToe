package s21.peanutsh.TicTacToe.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Builder
public class User {
    private UUID uuid;
    private String login;
    private String password;
    private Set<Role> roles;



    @Builder
    public User(String login, String password, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.roles = roles;
        this.uuid =UUID.randomUUID();
    }
}
