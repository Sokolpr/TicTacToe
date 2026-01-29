package s21.peanutsh.TicTacToe.datasource.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
public class EntityPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;
    private String password;
    private UUID uuid;


    public EntityPerson(String login, String password ) {
        this.password = password;
        this.uuid = UUID.randomUUID();
        this.login = login;
    }

}
