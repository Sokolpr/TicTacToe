package s21.peanutsh.TicTacToe.web.model;


import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class WinModel implements Comparable<WinModel> {
    private UUID uuid;
    private Double percentWinning;

    @Override
    public int compareTo(WinModel o) {
        return (int) (this.percentWinning * 100 - o.percentWinning * 100);
    }


}
