package s21.peanutsh.TicTacToe.di;

import org.springframework.context.annotation.Bean;
import s21.peanutsh.TicTacToe.datasource.repository.ModelRepository;
import s21.peanutsh.TicTacToe.domain.service.GameService;
import s21.peanutsh.TicTacToe.domain.service.UserService;
import s21.peanutsh.TicTacToe.domain.service.impl.GameServiceImpl;
import s21.peanutsh.TicTacToe.web.controller.GameController;

@org.springframework.context.annotation.Configuration
public class GameConfiguration {
    @Bean
    public GameController gameController(GameService gameService) {
        return new GameController(gameService);
    }

    @Bean
    public GameService gameService(ModelRepository modelRepository, UserService userService) {
        return new GameServiceImpl(modelRepository, userService);
    }


}
