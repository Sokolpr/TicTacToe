package s21.peanutsh.TicTacToe.di;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import s21.peanutsh.TicTacToe.domain.service.AuthFilter;
import s21.peanutsh.TicTacToe.domain.service.ServiceAuth;
import s21.peanutsh.TicTacToe.domain.service.ServiceGame;
import s21.peanutsh.TicTacToe.web.controller.Controller;
import s21.peanutsh.TicTacToe.web.controller.ControllerAuth;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ServiceGame serviceGame() {
        return new ServiceGame();
    }

    @Bean
    public Controller controller() {
        return new Controller(serviceGame());
    }

    @Bean
    public ServiceAuth serviceAuth() {
        return new ServiceAuth(passwordEncoder());
    }

    @Bean
    public ControllerAuth controllerAuth() {
        return new ControllerAuth(serviceAuth());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
