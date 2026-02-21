package s21.peanutsh.TicTacToe.web.model;


import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WinModel {
    private UUID uuid;
    private Double percentWinning;
}
