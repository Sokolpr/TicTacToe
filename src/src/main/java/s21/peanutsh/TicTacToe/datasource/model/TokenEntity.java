package s21.peanutsh.TicTacToe.datasource.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "token")
public class TokenEntity {

    @Id
    private UUID uuid;
    private String token;


}
