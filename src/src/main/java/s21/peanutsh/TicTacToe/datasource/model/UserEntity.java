package s21.peanutsh.TicTacToe.datasource.model;

import jakarta.persistence.*;
import lombok.*;
import s21.peanutsh.TicTacToe.domain.model.Role;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "persons")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private UUID uuid;
    @Column(unique = true)
    private String login;
    private String password;
    private String roles;

}
