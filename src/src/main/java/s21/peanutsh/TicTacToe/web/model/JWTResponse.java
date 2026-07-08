package s21.peanutsh.TicTacToe.web.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JWTResponse {
    private String accessToken;
    private String refreshToken;
    private String type;

    @Builder
    public JWTResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.type = "Bearer";
    }
}
