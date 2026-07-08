package s21.peanutsh.TicTacToe.datasource.model;


import jakarta.persistence.*;
import lombok.*;
import s21.peanutsh.TicTacToe.domain.model.StateGame;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "models")
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Builder
public class ModelEntity {

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
    private LocalDate createDate;




}
