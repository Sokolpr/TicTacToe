package s21.peanutsh.TicTacToe.datasource.model;


import jakarta.persistence.*;
import lombok.*;
import s21.peanutsh.TicTacToe.domain.model.StateGame;

import java.util.UUID;

@Entity
@Table(name = "models")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EntityModel {

    @Id
    @Column(unique = true, nullable = false)
    private UUID uuidGame;
    private String field;
    private Boolean bot;
    private UUID firstPlayer;
    private UUID secondPlayer;
    @Enumerated(EnumType.STRING)
    private StateGame stateGame;
    private Boolean gameOver;


    @Builder
public EntityModel(UUID uuidGame, String field, Boolean bot, UUID firstPlayer, UUID secondPlayer, StateGame stateGame, boolean gameOver) {
        this.uuidGame = uuidGame;
        this.field = field;
        this.bot = bot;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.stateGame = stateGame;
        this.gameOver = gameOver;
    }

}
