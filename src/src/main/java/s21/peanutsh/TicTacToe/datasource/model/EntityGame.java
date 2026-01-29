package s21.peanutsh.TicTacToe.datasource.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import s21.peanutsh.TicTacToe.web.model.GameStatus;
import s21.peanutsh.TicTacToe.web.model.GameWinner;

import java.util.UUID;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_model")
public class EntityGame {
    @Id
    @Column(name="uuid_game")
    private UUID uuid;
    private String field;
    @Enumerated(EnumType.STRING)
    private GameStatus game_status;
    @Enumerated(EnumType.STRING)
    private GameWinner game_won;

}
